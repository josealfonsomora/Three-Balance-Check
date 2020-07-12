package com.josealfonsomora.threebalance.services

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

class ThreeService(private val threeLogInApi: ThreeLogInApi, private val threeApi: ThreeApi) {

    fun getUser() = threeApi.getUser()

    fun getBalance(salesChannel: String, subscriptionId: String, customerId: Long) =
        threeApi.getBalance(
            selfService = salesChannel,
            subscriptionId = subscriptionId,
            customerId = customerId
        )

    fun getAllowance(salesChannel: String, customerId: Long) =
        threeApi.getAllowance(selfService = salesChannel, customerId = customerId)

    fun login(email: String, password: String): Single<Response<ResponseBody>> =
        threeLogInApi.logIn(email, password)

}
