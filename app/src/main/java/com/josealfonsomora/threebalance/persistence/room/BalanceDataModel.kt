package com.josealfonsomora.threebalance.persistence.room

import androidx.room.*
import com.google.gson.Gson
import com.josealfonsomora.entity.ThreeBalance
import java.math.BigDecimal

@Entity(tableName = "balance")
data class BalanceDataModel(
    @PrimaryKey
    val balanceId: Long = 1, // We will store only one balance and replace it when saving a new one
    @Embedded(prefix = "buckets_")
    override val balances: List<BalanceBucketDataModel>,
    override val totalBalance: BigDecimal,
    val cachedTime: Long // Milliseconds,
) : ThreeBalance
