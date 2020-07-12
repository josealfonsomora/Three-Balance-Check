package com.josealfonsomora.threebalance.persistence

import androidx.room.Room
import com.josealfonsomora.threebalance.persistence.room.ThreeBalanceDB
import com.josealfonsomora.threebalance.repositories.ThreeBalanceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun providePersistenceModule() = module {
    single {
        Room.databaseBuilder(
            androidContext().applicationContext,
            ThreeBalanceDB::class.java,
            "three_balance_database"
        ).build()
    }

    single {
        ThreeBalanceRepository(
            threeService = get(),
            balanceDao = get<ThreeBalanceDB>().balanceDao()
        )
    }
}
