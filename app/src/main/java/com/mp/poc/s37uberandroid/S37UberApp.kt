package com.mp.poc.s37uberandroid

import android.app.Application
import com.google.maps.GeoApiContext
import com.mp.poc.s37mapsimulator.Simulator

class S37UberApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Simulator.geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()
    }
}