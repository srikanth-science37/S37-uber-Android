package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.ui.maps.MapsActivity
import com.mp.poc.s37uberandroid.utils.Utils
import com.mp.poc.s37uberandroid.utils.Variables
import kotlinx.android.synthetic.main.activity_screening_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ScreeningInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening_info)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        initViews()
        initClickListeners()

//        val viewModel = ViewModelProvider(this).get(EnRouteViewModel::class.java)
//        viewModel.isJourneyStarted().observe(this, {
//            if (it) {
//                btEnRoute.visibility = View.VISIBLE
//            }
//        })
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

    override fun onResume() {
        super.onResume()

        if (Variables.shouldTriggerNotification) {
            cancelTimerOperation()
            btEnRoute.visibility = View.VISIBLE
        } else {
            timerOperation()
        }
    }

    override fun onPause() {
        super.onPause()
        cancelTimerOperation()
    }

    private fun initViews() {
        val currentDateString =
            Utils.getEpochFromMillis(System.currentTimeMillis()) + "\n\n1:00 PM - 1:45 PM"
        tvWhen.text = currentDateString
    }

    private fun initClickListeners() {
        btEnRoute.setOnClickListener {
            goToMapScreen()
        }
    }

    private fun goToMapScreen() {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private fun timerOperation() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (Variables.shouldTriggerNotification) {
                    GlobalScope.launch(Dispatchers.Main) {
                        btEnRoute.visibility = View.VISIBLE
                    }
                    cancelTimerOperation()
                }
            }
        }
        timer?.scheduleAtFixedRate(timerTask!!, 0, 500)
    }

    private fun cancelTimerOperation() {
        timer?.cancel()
        timer = null
        timerTask = null
    }
}