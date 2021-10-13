package com.mp.poc.s37mapsimulator

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.properties.Delegates

object Simulator {

    private const val TAG = "Simulator"
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    lateinit var geoApiContext: GeoApiContext
    private lateinit var pickUpLocation: LatLng
    private lateinit var dropLocation: LatLng
    private var tripPath = arrayListOf<LatLng>()
    private val mainThread = Handler(Looper.getMainLooper())
    private var tripPeriod by Delegates.notNull<Long>()
    private lateinit var socketListener: WebSocketListener
    private var tripPathIndex = 0
    private var pathSize = 0

    fun requestCab(
        pickUpLocation: LatLng,
        dropLocation: LatLng,
        webSocketListener: WebSocketListener
    ) {
        Simulator.pickUpLocation = pickUpLocation
        Simulator.dropLocation = dropLocation

        startTimerForWaitDuringPickUp(webSocketListener)
    }

    private fun startTimerForWaitDuringPickUp(webSocketListener: WebSocketListener) {
        val delay = 300L
        val period = 300L
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                stopTimer()
                val jsonObjectCabArrived = JSONObject()
                jsonObjectCabArrived.put("type", "cabArrived")
                mainThread.post {
                    webSocketListener.onMessage(jsonObjectCabArrived.toString())
                }
                val directionsApiRequest = DirectionsApiRequest(geoApiContext)
                directionsApiRequest.mode(TravelMode.DRIVING)
                directionsApiRequest.origin(pickUpLocation)
                directionsApiRequest.destination(dropLocation)
                directionsApiRequest.setCallback(object :
                    PendingResult.Callback<DirectionsResult> {
                    override fun onResult(result: DirectionsResult) {
                        Log.d(TAG, "onResult : $result")
                        tripPath.clear()
                        val routeList = result.routes
                        // Actually it will have zero or 1 route as we haven't asked Google API for multiple paths

                        if (routeList.isEmpty()) {
                            val jsonObjectFailure = JSONObject()
                            jsonObjectFailure.put("type", "routesNotAvailable")
                            mainThread.post {
                                webSocketListener.onError(jsonObjectFailure.toString())
                            }
                        } else {
                            for (route in routeList) {
                                val path = route.overviewPolyline.decodePath()
                                tripPath.addAll(path)
                            }
                            startTimerForTrip(webSocketListener)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        Log.d(TAG, "onFailure : ${e.message}")
                        val jsonObjectFailure = JSONObject()
                        jsonObjectFailure.put("type", "directionApiFailed")
                        jsonObjectFailure.put("error", e.message)
                        mainThread.post {
                            webSocketListener.onError(jsonObjectFailure.toString())
                        }
                    }
                })

            }
        }
        timer?.schedule(timerTask, delay, period)
    }

    private fun startTimerForTrip(webSocketListener: WebSocketListener) {
        val delay = 1000L
        val period = 500L
        tripPeriod = period
        pathSize = tripPath.size
        tripPathIndex = 0
        socketListener = webSocketListener
        timer = Timer()
        timerTask = getTripTimerTask()
        timer?.schedule(timerTask, delay, period)
    }

    private fun getTripTimerTask(): TimerTask {
        return object : TimerTask() {
            override fun run() {
                Log.e("TestSim", tripPathIndex.toString())
                if (tripPathIndex == 0) {
                    val jsonObjectTripStart = JSONObject()
                    jsonObjectTripStart.put("type", "tripStart")
                    mainThread.post {
                        socketListener.onMessage(jsonObjectTripStart.toString())
                    }

                    val jsonObject = JSONObject()
                    jsonObject.put("type", "tripPath")
                    val jsonArray = JSONArray()
                    for (trip in tripPath) {
                        val jsonObjectLatLng = JSONObject()
                        jsonObjectLatLng.put("lat", trip.lat)
                        jsonObjectLatLng.put("lng", trip.lng)
                        jsonArray.put(jsonObjectLatLng)
                    }
                    jsonObject.put("path", jsonArray)
                    mainThread.post {
                        socketListener.onMessage(jsonObject.toString())
                    }
                }

//                if (tripPathIndex <= pathSize - 1) {
                    val jsonObject = JSONObject()
                    jsonObject.put("type", "location")
                    jsonObject.put("lat", tripPath[tripPathIndex].lat)
                    jsonObject.put("lng", tripPath[tripPathIndex].lng)
                    mainThread.post {
                        socketListener.onMessage(jsonObject.toString())
                    }
//                }

                if (tripPathIndex == pathSize - 1) {
                    stopTimer()
                    startTimerForTripEndEvent(socketListener)
                }

                tripPathIndex++
            }
        }
    }

    fun rescheduleExistingTripTime(period: Long) {
        if (tripPeriod == period || timer == null) return
        tripPeriod = period
        synchronized(timer!!) {
            timer?.cancel()
            timer = Timer()
            timerTask = getTripTimerTask()
            timer?.schedule(timerTask, 0, period)
        }
    }

    private fun startTimerForTripEndEvent(webSocketListener: WebSocketListener) {
        val delay = 3000L
        val period = 3000L
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                stopTimer()
                val jsonObjectTripEnd = JSONObject()
                jsonObjectTripEnd.put("type", "tripEnd")
                mainThread.post {
                    webSocketListener.onMessage(jsonObjectTripEnd.toString())
                }
            }
        }
        timer?.schedule(timerTask, delay, period)
    }

    fun stopTimer() {
        if (timer != null) {
            tripPeriod = 0L
            tripPathIndex = 0
            pathSize = 0
            timer?.cancel()
            timer = null
        }
    }
}