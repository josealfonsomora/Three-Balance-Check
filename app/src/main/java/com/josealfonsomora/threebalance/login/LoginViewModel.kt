package com.josealfonsomora.threebalance.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josealfonsomora.mvvmsample.core.extensions.disposeWith
import com.josealfonsomora.threebalance.R
import com.josealfonsomora.threebalance.services.LoginService
import com.josealfonsomora.threebalance.services.LoginData
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val loginService: LoginService,
    private val ioScheduler: Scheduler = Schedulers.io()
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Any>()
    val loginResult: LiveData<Any> get() = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        loginService
            .login(username, password)
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _loginResult.postValue(LoginData())
                }, {
                    _loginResult.postValue(LoginData())
                    Log.e("LoginViewModel", it.toString())
                }
            ).disposeWith(disposables)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
