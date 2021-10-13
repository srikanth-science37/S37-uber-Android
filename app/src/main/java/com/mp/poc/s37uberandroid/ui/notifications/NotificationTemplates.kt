package com.mp.poc.s37uberandroid.ui.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.mp.poc.s37uberandroid.BuildConfig
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.ui.maps.MapsActivity

/**
 * @NotificationTemplates Notification setup class for the Nurse journey
 */
class NotificationTemplates {
    companion object {
        const val NOTIFICATION_ID = BuildConfig.TRIP_NOTIFICATION_ID
        const val CHANNEL_ID = BuildConfig.TRIP_CHANNEL_ID
        const val CHANNEL_SERVICE = BuildConfig.TRIP_CHANNEL_NAME

        private const val PENDING_REQUEST_CODE = 13

        /**
         * Function to build notification
         */
        fun getTripNotification(
            context: Context,
            channel: String,
            message: String,
            description: String
        ): Notification {

            val intent = Intent(context, MapsActivity::class.java)

            val activityPendingIntent = PendingIntent.getActivity(
                context, PENDING_REQUEST_CODE,
                intent, 0
            )

            val builder = NotificationCompat.Builder(context, channel)
                .setContentTitle(message)
                .setContentText(description)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(description)
                )
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher_s37)
                .setContentIntent(activityPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(ContextCompat.getColor(context, R.color.black))

            return builder.build()
        }
    }
}
