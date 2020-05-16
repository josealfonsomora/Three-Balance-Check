package com.josealfonsomora.threebalance.config

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.josealfonsomora.threebalance.R
import com.josealfonsomora.threebalance.databinding.ActivityConfigBinding
import com.josealfonsomora.threebalance.factories.NotificationFactory
import com.josealfonsomora.threebalance.services.ThreeService
import com.josealfonsomora.threebalance.workers.CheckThreeBalanceWorker
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class ConfigActivity : ComponentActivity() {
    private val REQUEST_CODE = 0
    private val disposables = CompositeDisposable()

    private val threeService: ThreeService by inject()
    private val notificationManager: NotificationManager by inject()

    private lateinit var binding: ActivityConfigBinding
    private val viewModel: ConfigViewModel by viewModel()

    private val minute: Long = 60_000L
    private val second: Long = 1_000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_config)
        binding.lifecycleOwner = this
        binding.model = viewModel

        createNotificationChannelId()
    }

//    override fun onResume() {
//        super.onResume()
//        threeService
//            .getUser()
//            .flatMap { threeResponse ->
//                Single.zip(
//                    threeService.getBalance(
//                        threeResponse.salesChannel,
//                        threeResponse.customer.first().product.first().subscriptionId,
//                        threeResponse.customer.first().id
//                    ),
//                    threeService.getAllowance(
//                        threeResponse.salesChannel,
//                        threeResponse.customer.first().id
//                    ),
//                    BiFunction { balance: ThreeBalanceResponse, allowance: ThreeAllowanceResponse ->
//                        balance to allowance
//                    }
//                )
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ (balance, allowance) ->
//                Log.d("ConfigActivity", balance.toString())
//                Log.d("ConfigActivity", allowance.toString())
//                val constraints = Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build()
//
//                val workerInstance = PeriodicWorkRequest.Builder(
//                    CheckThreeBalanceWorker::class.java, 5, TimeUnit.MINUTES
//                )
//                    .setConstraints(constraints)
//                    .build()
//
//                WorkManager.getInstance(this).enqueue(workerInstance)
//            }, {
//                Log.e("ConfigActivity", it.toString())
//            }).disposeWith(disposables)
//    }

    override fun onResume() {
        super.onResume()
        Log.d("ConfigActivity", "Starting worker")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val now = LocalDateTime.now()
        val eightPM = LocalDateTime.of(
            now.year,
            now.month,
            now.dayOfMonth,
            21,
            30,
            0,
            0
        )

        WorkManager.getInstance(this).enqueue(OneTimeWorkRequest.Builder(CheckThreeBalanceWorker::class.java).setConstraints(constraints).build())

        val seconds = now.until(eightPM, ChronoUnit.SECONDS)
        Log.d("ConfigActivity", "seconds til 22:30 $seconds -> minutes ${seconds/60}")

        val workerInstance = PeriodicWorkRequest.Builder(
            CheckThreeBalanceWorker::class.java, 15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setScheduleRequestedAt(seconds, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workerInstance)

    }

    private fun createNotificationChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NotificationFactory.CHANNEL_ID,
                "three notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                description = "Three Balance notification channel"
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        fun launch(activity: Activity) {
            activity.startActivity(Intent(activity, ConfigActivity::class.java))
        }
    }
}
