package com.josealfonsomora.threebalance.login

import android.content.SharedPreferences
import androidx.lifecycle.Observer
import com.josealfonsomora.threebalance.InstantTaskExecutorRule
import com.josealfonsomora.threebalance.services.LoginService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response

@ExtendWith(InstantTaskExecutorRule::class)
class LoginViewModelTest {

    private val email = "user@email.com"
    private val password = "password"
    private val response302: Response<ResponseBody> = mock {
        on { code() }.thenReturn(302)
    }

    private val response200: Response<ResponseBody> = mock {
        on { code() }.thenReturn(200)
    }

    private val loginService: LoginService = mock {
        on { login(email, password) }.thenReturn(Single.just(response302))
    }
    private val editor: SharedPreferences.Editor = mock {
        on { putString(any(), any()) }.thenReturn(it)
    }
    private val sharedPreferences: SharedPreferences = mock {
        on { edit() }.thenReturn(editor)
    }

    private val viewModel = LoginViewModel(
        loginService = loginService,
        sharedPreferences = sharedPreferences,
        ioScheduler = Schedulers.trampoline(),
        uiScheduler = Schedulers.trampoline()
    )

    @Test
    fun emitsASuccessLoginStateWhenErrorCode302FromTheServer() {
        val observer: Observer<Any> = mock()

        viewModel.loginResult.observeForever(observer)

        viewModel.login(email, password)

        assert(viewModel.loginResult.value is LoginResult.Success)
    }

    @Test
    fun emitsAErrorLoginStateWhenErrorCode200FromTheServer() {
        val observer: Observer<Any> = mock()
        whenever(loginService.login(email, password)).thenReturn(Single.just(response200))
        viewModel.loginResult.observeForever(observer)

        viewModel.login(email, password)

        assert(viewModel.loginResult.value is LoginResult.Error)
    }
}
