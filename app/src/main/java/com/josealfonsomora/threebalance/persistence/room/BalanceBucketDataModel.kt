package com.josealfonsomora.threebalance.persistence.room

import androidx.room.PrimaryKey
import com.josealfonsomora.entity.ThreeBalance
import java.math.BigDecimal

data class BalanceBucketDataModel(
    @PrimaryKey
    override val id: Long,
    override val amount: BigDecimal,
    override val expiryDate: Long,
    override val currency: String,
    override val subscriptionId: String,
    override val daysToExpire: Int,
    override val name: String
) : ThreeBalance.Bucket
