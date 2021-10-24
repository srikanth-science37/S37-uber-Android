package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.utils.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch(Dispatchers.Main) {
            delay(1500)
            screenTransition()
        }
    }

    private fun screenTransition() {
        val intentClass = if (Preference.isLogin(this)) HomeActivity::class.java else
            LoginActivity::class.java
        startActivity(Intent(this, intentClass))
        finish()
    }
}