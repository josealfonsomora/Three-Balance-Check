package com.josealfonsomora.threebalance.persistence.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josealfonsomora.entity.ThreeBalance
import io.reactivex.Completable

@Dao
interface BalanceDao {
    @Query("SELECT * FROM balance LIMIT 1")
    fun loadBalance(): BalanceDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalance(balanceDataModel: BalanceDataModel): Completable
}
