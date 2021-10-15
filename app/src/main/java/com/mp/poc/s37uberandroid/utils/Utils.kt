package com.mp.poc.s37uberandroid.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import java.text.SimpleDateFormat
import java.util.*

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

        // convert date to calendar
        val c = Calendar.getInstance()
        c.time = currentDate

        // manipulate date
        c.add(Calendar.DATE, 20) //same with c.add(Calendar.DAY_OF_MONTH, 1);

        val nextTime = c.time

        return dateFormat.format(nextTime)
    }

    fun getDeviceHeight(context: Context): Int {
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)

        return metrics.heightPixels
    }
}