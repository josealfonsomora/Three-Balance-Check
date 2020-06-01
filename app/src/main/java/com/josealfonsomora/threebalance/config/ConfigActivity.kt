package com.josealfonsomora.threebalance.config

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.josealfonsomora.threebalance.R
import com.josealfonsomora.threebalance.databinding.ActivityConfigBinding
import com.josealfonsomora.threebalance.factories.CHANNEL_ID
import com.josealfonsomora.threebalance.factories.NotificationFactory
import com.josealfonsomora.threebalance.workers.CheckThreeBalanceWorker
import kotlinx.android.synthetic.main.activity_config.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class ConfigActivity : ComponentActivity() {
    private val notificationManager: NotificationManager by inject()

    private lateinit var binding: ActivityConfigBinding
    private val viewModel: ConfigViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_config)
        binding.lifecycleOwner = this
        binding.model = viewModel

        createNotificationChannelId()
    }

    override fun onStart() {
        super.onStart()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        checkBalanceButton.setOnClickListener {
            checkBalanceButton.visibility = View.INVISIBLE
            showBalanceOneTime(constraints)
            scheduleBalanceEveryMorning(constraints)
        }
    }

    private fun scheduleBalanceEveryMorning(constraints: Constraints) {
        val now = LocalDateTime.now()
        val tomorrow =
            LocalDateTime.of(now.year, now.month, now.plusDays(1).dayOfMonth, 11, 0, 0, 0)

        val delay = now.until(tomorrow, ChronoUnit.SECONDS)

        Log.d("ConfigActivity", "delay of $delay seconds" )
        val workerInstance =
            PeriodicWorkRequest.Builder(CheckThreeBalanceWorker::class.java, 24 * 60, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(this).enqueue(workerInstance)
    }

    private fun showBalanceOneTime(constraints: Constraints) {
        WorkManager.getInstance(this)
            .enqueue(
                OneTimeWorkRequest.Builder(
                    CheckThreeBalanceWorker::class.java
                ).setConstraints(constraints)
                    .build()
            )
    }

    private fun createNotificationChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
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
