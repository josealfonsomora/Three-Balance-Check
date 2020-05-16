package com.josealfonsomora.threebalance.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences may vary.
 */
class AddCookiesInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val cookies = sharedPreferences.getStringSet("cookies", HashSet<String>())
        cookies?.let {
            for (cookie in it) {
                builder.addHeader("Cookie", cookie)
                Log.v(
                    "OkHttp",
                    "Adding Header: $cookie"
                ) // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }
        }
        return chain.proceed(builder.build())
    }
}

/**
 * This Interceptor add all received Cookies to the app DefaultPreferences.
 * Your implementation on how to save the Cookies on the Preferences may vary.
 */
class ReceivedCookiesInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = sharedPreferences.getStringSet("cookies", HashSet<String>())?: HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            sharedPreferences.edit().putStringSet("cookies", cookies).apply()
        }
        return originalResponse
    }
}
