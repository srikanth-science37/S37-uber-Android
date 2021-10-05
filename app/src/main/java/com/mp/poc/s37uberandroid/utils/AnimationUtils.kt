package com.mp.poc.s37uberandroid.utils

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

object AnimationUtils {

    fun polyLineAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(0, 100)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1500
        return valueAnimator
    }

    fun cabAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 1200
        valueAnimator.interpolator = LinearInterpolator()
        return valueAnimator
    }

}