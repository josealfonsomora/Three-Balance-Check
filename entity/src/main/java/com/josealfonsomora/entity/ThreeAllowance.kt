package com.josealfonsomora.entity

import java.math.BigDecimal

interface ThreeAllowance {
    val cycleEndDate: String // Fri Jun 19 00:00:00 IST 2020
    val cycleStartDate: String  // Thu Mar 19 00:00:00 GMT 2020
    val daysToNextBill: Int
    val accumulators: List<Accumulator>

    interface Accumulator{
        val id: Long
        val currency: String
        val amount: BigDecimal
        val name: String
        val pricePackageID: Long
        val quota: BigDecimal
        val remainingQuota: BigDecimal
        val subscriptionId: Long
        val type: String
        val unitOfMeasurement: String
        val allowanceType: String
        val usageType: String
        val utilizedQuotaPercentage: Float
        val volume: Float
        val description: String
        val remainingQuotaPercentage: Float
        val expirationDate: String //2222-01-01T19:00:00Z
    }
}
