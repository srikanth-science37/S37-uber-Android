package com.mp.poc.s37uberandroid.utils

import android.content.Context
import android.graphics.*
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs
import kotlin.math.atan
import android.graphics.Canvas

import android.graphics.Bitmap

import androidx.core.content.ContextCompat
import com.mp.poc.s37uberandroid.R


object MapUtils {

    private const val TAG = "MapUtils"

    fun getCarBitmap(context: Context): Bitmap {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_car_01)

        val bitmap = Bitmap.createBitmap(
            35,
            65, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)

        return bitmap
    }

    fun getOriginBitmap(): Bitmap {
        val height = 20
        val width = 20
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

    fun getDestinationBitmap(context: Context): Bitmap {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_poin_d)

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun getTripFinishOriginDestinationBitmap(context: Context, isOrigin: Boolean): Bitmap {
        val drawable = if (isOrigin) ContextCompat.getDrawable(context, R.drawable.ic_source)
        else ContextCompat.getDrawable(context, R.drawable.ic_destination)

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun getRotation(start: LatLng, end: LatLng): Float {
        val latDifference: Double = abs(start.latitude - end.latitude)
        val lngDifference: Double = abs(start.longitude - end.longitude)
        var rotation = -1F
        when {
            start.latitude < end.latitude && start.longitude < end.longitude -> {
                rotation = Math.toDegrees(atan(lngDifference / latDifference)).toFloat()
            }
            start.latitude >= end.latitude && start.longitude < end.longitude -> {
                rotation = (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90).toFloat()
            }
            start.latitude >= end.latitude && start.longitude >= end.longitude -> {
                rotation = (Math.toDegrees(atan(lngDifference / latDifference)) + 180).toFloat()
            }
            start.latitude < end.latitude && start.longitude >= end.longitude -> {
                rotation =
                    (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270).toFloat()
            }
        }
        Log.d(TAG, "getRotation: $rotation")
        return rotation
    }
}