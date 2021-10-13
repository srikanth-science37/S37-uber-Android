package com.mp.poc.s37uberandroid

import android.app.Application
import android.os.Build
import com.google.maps.GeoApiContext
import com.mp.poc.s37mapsimulator.Simulator
import com.mp.poc.s37uberandroid.ui.notifications.S37NotificationManager
import com.mp.poc.s37uberandroid.utils.Variables
import com.mp.poc.s37uberandroid.viewmodel.EnRouteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class S37UberApp : Application() {

    private var notificationManager: S37NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        Simulator.geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()
    }

    fun setupNotification() {
//        EnRouteViewModel.setValue()
        Variables.shouldTriggerNotification = true
        if (notificationManager == null) notificationManager = S37NotificationManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager?.setupUserAlertNotificationChannel()
            setupNotificationType(S37NotificationManager.NotificationState.STARTED)
        } else {
            setupNotificationType(S37NotificationManager.NotificationState.STARTED)
        }
    }

    fun setupNotificationType(type: S37NotificationManager.NotificationState) {
        notificationManager?.notifyUser(type)
    }

    fun terminate() {
        Variables.shouldTriggerNotification = false
        notificationManager = null
    }

    override fun onTerminate() {
        super.onTerminate()
        terminate()
    }

    fun triggerFiveSecTimer() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(5000)
            setupNotification()
        }
    }

}