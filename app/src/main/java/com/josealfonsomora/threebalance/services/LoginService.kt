package com.josealfonsomora.threebalance.services

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single

// If user credentials will be cached in local storage, it is recommended it be encrypted
// @see https://developer.android.com/training/articles/keystore
class LoginService(
    private val api: ThreeLogInApi,
    sharedPreferences: SharedPreferences
) {

    fun login(username: String, password: String): Completable =
        api.logIn()
}
