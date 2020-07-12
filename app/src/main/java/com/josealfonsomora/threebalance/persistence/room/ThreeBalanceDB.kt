package com.josealfonsomora.threebalance.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BalanceDao::class], version = 1)
abstract class ThreeBalanceDB : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}
