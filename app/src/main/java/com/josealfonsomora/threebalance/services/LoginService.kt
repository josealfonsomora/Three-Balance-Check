package com.josealfonsomora.threebalance.services

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

class LoginService(
    private val api: ThreeLogInApi
) {
    fun login(email: String, password: String): Single<Response<ResponseBody>> =
        api.logIn(email, password)
}
