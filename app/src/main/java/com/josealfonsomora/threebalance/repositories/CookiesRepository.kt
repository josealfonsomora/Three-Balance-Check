package com.josealfonsomora.threebalance.repositories

import android.content.SharedPreferences
import com.google.gson.Gson
import okhttp3.Cookie

class CookiesRepository(private val sharedPreferences: SharedPreferences, private val gson: Gson) {
    fun saveCookies(cookies: MutableList<Cookie>) {
        val savedCookies = getCookies().map { it.name() to it }.toMap().toMutableMap()

        cookies.forEach { cookie ->
            savedCookies[cookie.name()] = cookie
        }

        sharedPreferences.edit()
            .putStringSet("cookies", savedCookies.map { gson.toJson(it.value) }.toHashSet())
            .apply()
    }

    fun getCookies(): MutableList<Cookie> =
        sharedPreferences.getStringSet("cookies", HashSet<String>())
            ?.map { gson.fromJson(it, Cookie::class.java) }
            ?.toMutableList() ?: mutableListOf()
}
