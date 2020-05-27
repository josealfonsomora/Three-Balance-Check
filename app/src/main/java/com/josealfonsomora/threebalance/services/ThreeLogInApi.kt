package com.josealfonsomora.threebalance.services

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ThreeLogInApi {
    @POST("/")
    fun logIn(
        @Query("username") username: String="EMAIL",
        @Query("password") password: String="PASSWORD",
        @Query("section") section: String ="section"
    ): Completable
}

class LoginData(val username: String, val password: String)
