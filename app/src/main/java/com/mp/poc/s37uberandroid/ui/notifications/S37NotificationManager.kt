package com.mp.poc.s37uberandroid.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.ui.notifications.S37NotificationManager.NotificationState.*

class S37NotificationManager(context: Context) {
    private var userAlertNotificationManager: NotificationManager? = null
    private var notificationShown: NotificationState? = null
    private val mContext = context

    /**
     * Called to setup notification channel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setupUserAlertNotificationChannel() {
        if (userAlertNotificationManager != null) return

        userAlertNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val name = NotificationTemplates.CHANNEL_SERVICE
        // Create the channel for the notification
        val mChannel =
            NotificationChannel(
                NotificationTemplates.CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        mChannel.enableVibration(true)
        mChannel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            audioAttributes
        )

        mChannel.setShowBadge(false)

        // Set the Notification Channel for the Notification Manager.
        userAlertNotificationManager?.createNotificationChannel(mChannel)
    }

    /**
     * @NotificationState enum for different notification state
     */
    enum class NotificationState {
        STARTED,
        ARRIVING_SHORTLY,
        ARRIVED
    }

    /**
     * Called to set Missing Config foreground notification
     */
    private fun notifyTripStarted() {
        if (notificationShown != STARTED) {
            val notif =
                NotificationTemplates.getTripNotification(
                    mContext,
                    NotificationTemplates.CHANNEL_ID,
                    "Journey started",
                    mContext.getString(R.string.you_are_on_a_trip)
                )
            userAlertNotificationManager?.notify(NotificationTemplates.NOTIFICATION_ID, notif)
            notificationShown = STARTED
        }
    }

    /**
     * Called to set the Service running foreground notification
     */
    private fun notifyArrivingShortly() {
        if (notificationShown != ARRIVING_SHORTLY) {
            val notif =
                NotificationTemplates.getTripNotification(
                    mContext,
                    NotificationTemplates.CHANNEL_ID,
                    "Arriving shortly",
                    mContext.getString(R.string.arriving_shortly)
                )
            userAlertNotificationManager?.notify(NotificationTemplates.NOTIFICATION_ID, notif)
            notificationShown = ARRIVING_SHORTLY
        }
    }

    /**
     * Called to set Distance alert foreground notification
     */
    private fun notifyArrived() {
        if (notificationShown != ARRIVED) {
            val notif = NotificationTemplates.getTripNotification(
                mContext,
                NotificationTemplates.CHANNEL_ID,
                "Reached the destination",
                "Jessica Marlin (Microbiology) has reached your location. Please wear a mask before you meet."
            )
            userAlertNotificationManager?.notify(NotificationTemplates.NOTIFICATION_ID, notif)
            notificationShown = ARRIVED
        }
    }

    /**
     * Set user notification
     * @param notificationState either STARTED, ARRIVING_SHORTLY or ARRIVED notification state
     */
    fun notifyUser(notificationState: NotificationState) {
        if (userAlertNotificationManager == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    setupUserAlertNotificationChannel()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        when (notificationState) {
            STARTED -> {
                if (notificationShown == STARTED) return
                notifyTripStarted()
            }
            ARRIVING_SHORTLY -> {
                if (notificationShown == ARRIVING_SHORTLY) return
                notifyArrivingShortly()
            }
            ARRIVED -> {
                if (notificationShown == ARRIVED) return
                notifyArrived()
            }
        }
    }
}