package com.mp.poc.s37uberandroid.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.mp.poc.s37uberandroid.R

object ViewUtils {

    fun enableTransparentStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val winParams = window.attributes
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.attributes = winParams
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    fun rotateView(fromDegrees: Float, toDegrees: Float, view: View) {
        val rotate = RotateAnimation(
            fromDegrees,
            toDegrees,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.interpolator = LinearInterpolator()
        rotate.isFillEnabled = true
        rotate.fillAfter = true
        view.startAnimation(rotate)
    }

    fun setGif(context: Context, imageView: ImageView, drawableResource: Int) {
        setImage(context, imageView, drawableResource)
    }

    fun setImage(ctx: Context, imageView: ImageView, resource: Int) {
        imageView.setImageDrawable(AppCompatResources.getDrawable(ctx, resource))
    }

    fun setAlpha(view: View, shouldEnable: Boolean) {
        view.alpha = if (shouldEnable) 1f else 0.3f
    }

    fun setButtonAlpha(context: Context, button: AppCompatButton, enable: Boolean) {
        setAlpha(button, enable)
        button.setTextColor(
            context.resources.getColor(
                if (enable) R.color.white else R.color.black, null
            )
        )
        button.isEnabled = enable
        button.isClickable = enable
    }

}