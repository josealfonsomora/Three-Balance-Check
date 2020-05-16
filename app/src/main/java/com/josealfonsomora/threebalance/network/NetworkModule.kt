package com.josealfonsomora.threebalance.network

import android.util.Log
import com.josealfonsomora.threebalance.repositories.CookiesRepository
import com.josealfonsomora.threebalance.services.ThreeLogInApi
import com.josealfonsomora.threebalance.services.ThreeApi
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Koin module for Network dependencies like Retrofit or OkHttp
 */
fun provideNetworkModule() = module {

    single { RxJava2CallAdapterFactory.create() }


    single {
        HttpLoggingInterceptor { message ->
            Log.d("Retrofit logging", message)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                    get<CookiesRepository>().saveCookies(cookies)
                }

                override fun loadForRequest(url: HttpUrl) =
                    if (url.toString().contains("https://login.three.ie/")) { // Do not use cookies at login
                        mutableListOf()
                    } else {
                        get<CookiesRepository>().getCookies()
                    }
            })
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        GsonConverterFactory.create(get())
    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .baseUrl("https://login.three.ie")
            .build()
            .create(ThreeLogInApi::class.java) as ThreeLogInApi
    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .baseUrl("https://www.three.ie")
            .build()
            .create(ThreeApi::class.java) as ThreeApi
    }


}
