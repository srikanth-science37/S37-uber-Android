package com.mp.poc.s37uberandroid.utils

import android.content.Context

/**
 * Shared Preference class for Science37 app
 */
object Preference {
    private const val PREF_ID = "S37_SharedPref"

    private const val IS_SIGNUP = "IS_SIGNUP"
    private const val IS_LOGIN = "IS_LOGIN"
    private const val TEMP_EMAIL = "TEMP_EMAIL"
    private const val TEMP_PASSWORD = "TEMP_PASSWORD"

    fun putIsSignUp(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .edit().putBoolean(IS_SIGNUP, value).apply()
    }

    fun isSignUp(context: Context): Boolean {
        return context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .getBoolean(IS_SIGNUP, false)
    }

    fun putIsLogin(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .edit().putBoolean(IS_LOGIN, value).apply()
    }

    fun isLogin(context: Context): Boolean {
        return context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .getBoolean(IS_LOGIN, false)
    }

    fun putEmailId(context: Context, value: String) {
        context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .edit().putString(TEMP_EMAIL, value).apply()
    }

    fun getEmailId(context: Context): String? {
        return context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .getString(TEMP_EMAIL, null)
    }

    fun putPassword(context: Context, value: String) {
        context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .edit().putString(TEMP_PASSWORD, value).apply()
    }

    fun getPassword(context: Context): String? {
        return context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)
            .getString(TEMP_PASSWORD, null)
    }
}
