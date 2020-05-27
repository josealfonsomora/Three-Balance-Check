package com.josealfonsomora.threebalance.workers

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.josealfonsomora.threebalance.factories.NotificationFactory
import com.josealfonsomora.threebalance.services.LoginService
import com.josealfonsomora.threebalance.services.ThreeAllowanceResponse
import com.josealfonsomora.threebalance.services.ThreeBalanceResponse
import com.josealfonsomora.threebalance.services.ThreeService
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


class CheckThreeBalanceWorker(context: Context, workerParams: WorkerParameters) :
    RxWorker(context, workerParams), KoinComponent {

    private val loginService: LoginService by inject()
    private val threeService: ThreeService by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val notificationManager: NotificationManager by inject()

    override fun createWork(): Single<Result> {

        val username = sharedPreferences.getString("email", "")!!
        val password = sharedPreferences.getString("password", "")!!

        return loginService
            .login(username, password)
            .andThen(threeService.getUser())
            .map {
                Triple(
                    it.salesChannel,
                    it.customer.first().id,
                    it.customer.first().product.first().subscriptionId
                )
            }
            .flatMap { (channel, customerId, subscriptionId) ->
                Single.zip(
                    threeService.getBalance(channel, subscriptionId, customerId),
                    threeService.getAllowance(channel, customerId),
                    BiFunction { balance: ThreeBalanceResponse, allowance: ThreeAllowanceResponse -> balance to allowance }
                )
            }.map { (balance, allowance) ->
                notificationManager.cancelAll()
                notificationManager.notify(
                    NotificationFactory.NOTIFICATION_ID,
                    NotificationFactory.newNotification(
                        title = "Three Balance",
                        content = "${balance.totalBalance} ${balance.buckets.firstOrNull()?.currency} untli ${balance.buckets.firstOrNull()?.balanceExpiryDate?.toFormattedDate()}",
                        largeContent = "Balance:\n ${balance.buckets.firstOrNull()?.toString()}\n Allowance:\n ${allowance.accumulators.firstOrNull()?.toString()}",
                        context = applicationContext,
                        notificationChannel = NotificationFactory.CHANNEL_ID
                    )
                )

                Result.success()
            }
    }
}

fun Long.toFormattedDate() =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault()).toString()

