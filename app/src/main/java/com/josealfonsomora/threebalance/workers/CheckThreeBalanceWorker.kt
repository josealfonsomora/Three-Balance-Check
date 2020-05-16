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
import java.time.LocalDateTime

class CheckThreeBalanceWorker(context: Context, workerParams: WorkerParameters) :
    RxWorker(context, workerParams), KoinComponent {

    private val loginService: LoginService by inject()
    private val threeService: ThreeService by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val notificationManager: NotificationManager by inject()

    override fun createWork(): Single<Result> {
        Log.d("CheckThreeBalanceServic", "createWork()")

        val username = sharedPreferences.getString("username", "EMAIL")!!
        val password = sharedPreferences.getString("password", "PASSWORD")!!

        setPeriodicWorkerForEveryMorning()

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
                Log.d("CheckThreeBalanceServic", "login success")
                Single.zip(
                    threeService.getBalance(channel, subscriptionId, customerId),
                    threeService.getAllowance(channel, customerId),
                    BiFunction { balance: ThreeBalanceResponse, allowance: ThreeAllowanceResponse -> balance to allowance }
                )
            }.map { (balance, allowance) ->
                Log.d("CheckThreeBalanceServic", "sending notification")
                notificationManager.cancelAll()
                notificationManager.notify(
                    NotificationFactory.NOTIFICATION_ID,
                    NotificationFactory.newSimpleNotification(
                        title = "Three Balance",
                        content = "Total balance ${balance.totalBalance} expiration date ${balance.buckets.firstOrNull()?.balanceExpiryDate}\n Allowance expiration date ${allowance.accumulators.firstOrNull()?.expirationDate}",
                        context = applicationContext,
                        notificationChannel = NotificationFactory.CHANNEL_ID
                    )
                )

                Result.success()
            }
    }

    private fun setPeriodicWorkerForEveryMorning() {

    }
}
