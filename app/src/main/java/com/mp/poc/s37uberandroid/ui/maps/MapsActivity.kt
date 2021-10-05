package com.mp.poc.s37uberandroid.ui.maps

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.network.NetworkService
import com.mp.poc.s37uberandroid.utils.AnimationUtils
import com.mp.poc.s37uberandroid.utils.MapUtils
import com.mp.poc.s37uberandroid.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.bottom_sheet.*

class MapsActivity : AppCompatActivity(), MapsView, OnMapReadyCallback {

    private lateinit var presenter: MapsPresenter
    private lateinit var googleMap: GoogleMap
    private lateinit var sourceLatLng: LatLng
    private var destinationLatLng: LatLng? = null
    private var destinationMarker: Marker? = null
    private var originMarker: Marker? = null
    private var greyPolyLine: Polyline? = null
    private var blackPolyline: Polyline? = null
    private var previousLatLngFromServer: LatLng? = null
    private var currentLatLngFromServer: LatLng? = null
    private var movingCabMarker: Marker? = null
    private var pathEstimateTime = 0    // Seconds unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        ViewUtils.enableTransparentStatusBar(window)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter = MapsPresenter(NetworkService())
        presenter.onAttach(this)
    }

    private fun setDefaultLatLng() {
        /*// Test markers only (short trip)
        sourceLatLng = LatLng(37.345470000000006, -121.95588000000001)
        destinationLatLng = LatLng(37.3579, -121.95437000000001)*/

        sourceLatLng = LatLng(34.019394, -118.4912687)
        destinationLatLng = LatLng(34.1693986, -118.8358302)

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceLatLng, 10f))
    }

    private fun drawDefaultPath() {
        if (greyPolyLine != null && greyPolyLine?.isVisible!!) greyPolyLine?.remove()
        if (blackPolyline != null && blackPolyline?.isVisible!!) blackPolyline?.remove()

        presenter.requestCab(sourceLatLng, destinationLatLng!!)
    }

    private fun animateCameraWithinBounds(pathLatLngList: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (latLng in pathLatLngList) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
    }

    private fun animateCameraZoomWithinBounds(pathLatLngList: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (latLng in pathLatLngList) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.center, 16.3f))
    }

    private fun addCarMarkerAndGet(latLng: LatLng): Marker? {
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(this))
        return googleMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )
    }

    private fun addOriginDestinationMarkerAndGet(latLng: LatLng, isOrigin: Boolean): Marker? {
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
            if (isOrigin) MapUtils.getOriginBitmap() else MapUtils.getDestinationBitmap(this)
        )
        return if (!isOrigin) googleMap.addMarker(
            MarkerOptions().position(latLng).title("45").snippet("Mins away")
                .icon(bitmapDescriptor)
        ) else googleMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )
    }

    private fun updateOriginDestinationMarkers(
        isOrigin: Boolean
    ): BitmapDescriptor? {
        return BitmapDescriptorFactory.fromBitmap(
            MapUtils.getTripFinishOriginDestinationBitmap(this, isOrigin)
        )
    }

    private fun reset() {
        previousLatLngFromServer = null
        currentLatLngFromServer = null
        movingCabMarker?.remove()
        greyPolyLine?.remove()
        blackPolyline?.remove()
        originMarker?.remove()
        destinationMarker?.remove()
        destinationLatLng = null
        greyPolyLine = null
        blackPolyline = null
        originMarker = null
        destinationMarker = null
        movingCabMarker = null
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        setDefaultLatLng()
        drawDefaultPath()
    }

    override fun showPath(latLngList: List<LatLng>) {
        Log.e(MapsActivity::class.java.simpleName, "shoPath() called!")
        val builder = LatLngBounds.Builder()
        for (latLng in latLngList) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 2))
        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.TRANSPARENT)
        polylineOptions.width(8f)
        polylineOptions.addAll(latLngList)
        greyPolyLine = googleMap.addPolyline(polylineOptions)

        val blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(8f)
        blackPolylineOptions.color(Color.BLACK)
        blackPolyline = googleMap.addPolyline(blackPolylineOptions)

        originMarker = addOriginDestinationMarkerAndGet(latLngList[0], true)
        originMarker?.setAnchor(0.5f, 0.5f)
        destinationMarker = addOriginDestinationMarkerAndGet(latLngList[latLngList.size - 1], false)
        destinationMarker?.setAnchor(0.5f, 0.5f)

        val polylineAnimator = AnimationUtils.polyLineAnimator()
        polylineAnimator.addUpdateListener { valueAnimator ->
            val percentValue = (valueAnimator.animatedValue as Int)
            val index = (greyPolyLine?.points!!.size * (percentValue / 100.0f)).toInt()
            blackPolyline?.points = greyPolyLine?.points!!.subList(0, index)
        }
        polylineAnimator.start()

        googleMap.setInfoWindowAdapter(CustomInfoWindow(this))
    }

    override fun updateCabLocation(latLng: LatLng) {
        if (movingCabMarker == null) {
            movingCabMarker = addCarMarkerAndGet(latLng)
        }
        if (previousLatLngFromServer == null) {
            currentLatLngFromServer = latLng
            previousLatLngFromServer = currentLatLngFromServer
            movingCabMarker?.position = currentLatLngFromServer!!
            movingCabMarker?.setAnchor(0.5f, 0.5f)
        } else {
            pathEstimateTime = (blackPolyline?.points?.size!! * 3) - 5
            if (pathEstimateTime == 0) {
                googleMap.setOnMarkerClickListener {
                    it.hideInfoWindow()
                    return@setOnMarkerClickListener true
                }
            }
            previousLatLngFromServer = currentLatLngFromServer
            currentLatLngFromServer = latLng
            val valueAnimator = AnimationUtils.cabAnimator()
            valueAnimator.addUpdateListener { va ->
                if (currentLatLngFromServer != null && previousLatLngFromServer != null) {
                    val multiplier = va.animatedFraction
                    val nextLocation = LatLng(
                        multiplier * currentLatLngFromServer!!.latitude + (1 - multiplier) * previousLatLngFromServer!!.latitude,
                        multiplier * currentLatLngFromServer!!.longitude + (1 - multiplier) * previousLatLngFromServer!!.longitude
                    )
                    movingCabMarker?.position = nextLocation
                    movingCabMarker?.setAnchor(0.5f, 0.5f)
                    val rotation = MapUtils.getRotation(previousLatLngFromServer!!, nextLocation)
                    if (!rotation.isNaN()) {
                        movingCabMarker?.rotation = rotation
                    }
                }
            }
            valueAnimator.start()

            val pathLatLngList = blackPolyline?.points
            val indexOfNextLocation = pathLatLngList?.indexOf(previousLatLngFromServer)
            for (index in 0..indexOfNextLocation!!) {
                if (index != indexOfNextLocation)
                    pathLatLngList.removeAt(index)
            }
            blackPolyline?.points = pathLatLngList

            if (pathLatLngList.size > 15) {
                animateCameraWithinBounds(pathLatLngList)
            } else {
                animateCameraZoomWithinBounds(pathLatLngList)
            }

            val pathETInMinutes = pathEstimateTime / 60
            val time = if (pathETInMinutes == 0) "1" else pathETInMinutes.toString()
            destinationMarker?.title = time
            destinationMarker?.snippet = if (pathETInMinutes <= 1) "Min away" else "Mins away"
            destinationMarker?.showInfoWindow()
            val bottomSheetEtaText =
                "Arriving in $time ${if (pathETInMinutes <= 1) "min" else "mins"}"
            tvEta.text = bottomSheetEtaText
        }
    }

    override fun informCabIsArriving() {
//        statusTextView.text = getString(R.string.your_cab_is_arriving)
    }

    override fun informCabArrived() {
        greyPolyLine?.remove()
        blackPolyline?.remove()
        originMarker?.remove()
        destinationMarker?.remove()
    }

    override fun informTripStart() {
        tvEta.text = ""
        previousLatLngFromServer = null
        dummyImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.slice_bottom_sheet_1
            )
        )
    }

    override fun informTripEnd() {
        dummyImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.slice_bottom_sheet_2
            )
        )
        val arrivedText = "Arrived"
        tvEta.text = arrivedText
        greyPolyLine?.color = Color.BLACK
        animateCameraWithinBounds(greyPolyLine?.points!!)
        blackPolyline?.remove()
        originMarker?.setIcon(updateOriginDestinationMarkers(true))
        destinationMarker?.setIcon(updateOriginDestinationMarkers(false))
        destinationMarker?.hideInfoWindow()
        movingCabMarker?.remove()
    }

    override fun showRoutesNotAvailableError() {
        val error = getString(R.string.route_not_available_choose_different_locations)
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        reset()
    }

    override fun showDirectionApiFailedError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        reset()
    }

}
