package com.josealfonsomora.threebalance

import android.app.Application
import com.josealfonsomora.threebalance.di.setupKoin

class ThreeBalanceApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }
}
