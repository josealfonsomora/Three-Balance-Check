package com.josealfonsomora.threebalance.di

import com.josealfonsomora.threebalance.ThreeBalanceApplication
import com.josealfonsomora.threebalance.config.provideConfigModule
import com.josealfonsomora.threebalance.network.provideNetworkModule
import com.josealfonsomora.threebalance.login.provideLoginModule
import com.josealfonsomora.threebalance.persistence.providePersistenceModule
import com.josealfonsomora.threebalance.repositories.provideRepositoryModule
import com.josealfonsomora.threebalance.services.provideServiceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Extension function to start koin dependency injector
 * This function
 */
fun ThreeBalanceApplication.setupKoin() {
    startKoin {
        androidLogger()
        androidContext(this@setupKoin)
        modules(
            listOf(
                provideCoreModule(),
                providePersistenceModule(),
                provideRepositoryModule(),
                provideNetworkModule(),
                provideServiceModule(),
                provideLoginModule(),
                provideConfigModule()
            )
        )
    }
}
