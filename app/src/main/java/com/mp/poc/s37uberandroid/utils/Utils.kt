package com.mp.poc.s37uberandroid.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.util.DisplayMetrics
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Utils {
    /**
     * Get string epoch from millis
     * @param milliSeconds timestamp millis
     * @return current epoch in "MMM dd, yyyy" format
     */
    fun getEpochFromMillis(milliSeconds: Long): String {
        val dateFormat = "MMM dd, yyyy"
        val formatter = SimpleDateFormat(dateFormat)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun nextEpoch(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy")

        val currentDate = Date()

        val c = Calendar.getInstance()
        c.time = currentDate

        c.add(Calendar.DATE, 20)

        val nextTime = c.time

        return dateFormat.format(nextTime)
    }

    fun getDeviceHeight(context: Context): Int {
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)

        return metrics.heightPixels
    }

    fun isValidEmail(email: CharSequence): Boolean {
//        val validEmailPattern =
//            Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: CharSequence): Boolean {
        val validPassword = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{6,}$")
        return validPassword.matcher(password).matches()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}