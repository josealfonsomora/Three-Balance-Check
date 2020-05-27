package com.josealfonsomora.threebalance.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            get<ThreeBalanceApplication>(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
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
