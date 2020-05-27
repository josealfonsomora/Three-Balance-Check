package com.josealfonsomora.threebalance.services

import io.reactivex.Completable

class LoginService(
    private val api: ThreeLogInApi
) {
    fun login(username: String, password: String): Completable =
        api.logIn(username, password)
}
