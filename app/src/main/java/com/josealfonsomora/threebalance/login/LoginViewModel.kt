package com.josealfonsomora.threebalance.login

import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josealfonsomora.mvvmsample.core.extensions.disposeWith
import com.josealfonsomora.threebalance.R
import com.josealfonsomora.threebalance.services.LoginService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val loginService: LoginService,
    private val sharedPreferences: SharedPreferences,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Any>()
    val loginResult: LiveData<Any> get() = _loginResult

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        loginService
            .login(email, password)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { response ->
                if (response.code() == 300) {
                    sharedPreferences.edit().putString("email", email).apply()
                    sharedPreferences.edit().putString("password", password).apply()
                    _loginResult.postValue(LoginResult.Success())
                } else {
                    _loginResult.postValue(LoginResult.Error())
                }
            }
            .disposeWith(disposables)
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

sealed class LoginResult(){
    class Success()
    class Error()
}
