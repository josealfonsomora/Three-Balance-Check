package com.josealfonsomora.entity

import java.math.BigDecimal

interface ThreeBalance {
    val balances: List<ThreeBalance.Bucket>
    val totalBalance: BigDecimal

    interface Bucket {
        val id: Long
        val amount: BigDecimal
        val expiryDate: Long
        val currency: String
        val subscriptionId: String
        val daysToExpire: Int
        val name: String
    }
}
