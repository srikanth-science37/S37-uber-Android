package com.mp.poc.s37uberandroid.ui.maps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.mp.poc.s37uberandroid.R


class CustomInfoWindow(var context: Context) : InfoWindowAdapter {
    var inflater: LayoutInflater? = null
    override fun getInfoContents(marker: Marker?): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // R.layout.echo_info_window is a layout in my
        // res/layout folder. You can provide your own
        val v: View = inflater!!.inflate(R.layout.eta_label_layout, null)
        val minutesUnit = v.findViewById<View>(R.id.eta_minutes_unit) as TextView
        val unitLabel = v.findViewById<View>(R.id.eta_unit_label) as TextView
        minutesUnit.text = marker.title
        unitLabel.text = marker.snippet
        return v
    }
}