package com.josealfonsomora.threebalance.services

import com.josealfonsomora.threebalance.services.ThreeApi

class ThreeService(private val threeApi: ThreeApi) {

    fun getUser() = threeApi.getUser()

    fun getBalance(salesChannel: String, subscriptionId: String, customerId: Long) =
        threeApi.getBalance(
            selfService = salesChannel,
            subscriptionId = subscriptionId,
            customerId = customerId
        )

    fun getAllowance(salesChannel: String, customerId: Long) =
        threeApi.getAllowance(selfService = salesChannel, customerId = customerId)
}
