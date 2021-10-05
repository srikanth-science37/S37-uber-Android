package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.ui.maps.MapsActivity
import kotlinx.android.synthetic.main.activity_screening_info.*

class ScreeningInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening_info)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        initClickListeners()
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initClickListeners() {
        btEnRoute.setOnClickListener {
            goToMapScreen()
        }
    }

    private fun goToMapScreen() {
        startActivity(Intent(this, MapsActivity::class.java))
    }
}