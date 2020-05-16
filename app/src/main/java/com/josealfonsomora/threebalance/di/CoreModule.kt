package com.josealfonsomora.threebalance.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.josealfonsomora.threebalance.ThreeBalanceApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Koin module for Core dependencies
 */
fun provideCoreModule() = module {
    single { androidApplication() as ThreeBalanceApplication }

    single {
        get<ThreeBalanceApplication>().getSharedPreferences("network", Context.MODE_PRIVATE)
    }

    single {
        ContextCompat.getSystemService(
            get(),
            NotificationManager::class.java
        ) as NotificationManager
    }

    single {
        get<ThreeBalanceApplication>().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    single { GsonBuilder().create() }

}
