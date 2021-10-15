package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.S37UberApp
import com.mp.poc.s37uberandroid.model.HomeRecyclerViewModel
import com.mp.poc.s37uberandroid.ui.adapter.HomeRecyclerViewAdapter
import com.mp.poc.s37uberandroid.utils.Utils
import com.mp.poc.s37uberandroid.utils.Variables
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), HomeRecyclerViewAdapter.OnHomeRecyclerItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (applicationContext as S37UberApp).triggerFiveSecTimer()
        todayTaskList()
        futureTaskList()

//        val viewModel = ViewModelProvider(this).get(EnRouteViewModel::class.java)
//        viewModel.isJourneyStarted().observe(this, {
//            (tasksRecyclerView.adapter as HomeRecyclerViewAdapter).getItems()[0].isEnRoute = it
//            tasksRecyclerView.adapter?.notifyDataSetChanged()
//        })
    }

    override fun onResume() {
        super.onResume()

        if (Variables.shouldTriggerNotification) {
            cancelTimerOperation()
            (tasksRecyclerView.adapter as HomeRecyclerViewAdapter).getItems()[0].isEnRoute = true
            tasksRecyclerView.adapter?.notifyDataSetChanged()
        } else {
            timerOperation()
        }
    }

    override fun onPause() {
        super.onPause()
        cancelTimerOperation()
    }

    private fun todayTaskList() {
        val data = mutableListOf<HomeRecyclerViewModel>()

        val currentDateString = Utils.getEpochFromMillis(System.currentTimeMillis())

        for (i in 1..3) {
            val model =
                HomeRecyclerViewModel(
                    "Screening Visit $i", "$currentDateString - $i:00 PM"
                )
            data.add(model)
        }

        val dividerItemDecoration = DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.home_recycler_view_divider,
                null
            )!!
        )
        tasksRecyclerView.addItemDecoration(dividerItemDecoration)

        tasksRecyclerView.adapter = HomeRecyclerViewAdapter(data, this)
    }

    private fun futureTaskList() {
        val data = mutableListOf<HomeRecyclerViewModel>()

        val nextDateString = Utils.nextEpoch()

        val model = HomeRecyclerViewModel("Quality of Life Assessment", "$nextDateString - 4:00 PM")
        data.add(model)

        futureTasksRecyclerView.adapter = HomeRecyclerViewAdapter(data, this)
    }

    override fun onItemClick(itemIndex: Int) {
        when (itemIndex) {
            0 -> goToDetailScreen()
            //            (futureTasksRecyclerView.adapter as HomeRecyclerViewAdapter).getItems()[itemIndex].isEnRoute -> goToDetailScreen()
//            else -> Toast.makeText(this, "Item ${itemIndex + 1} clicked!", Toast.LENGTH_SHORT)
//                .show()
        }
    }

    private fun goToDetailScreen() {
        startActivity(Intent(this, ScreeningInfoActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        (applicationContext as S37UberApp).terminate()
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private fun timerOperation() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (Variables.shouldTriggerNotification) {
                    GlobalScope.launch(Dispatchers.Main) {
                        (tasksRecyclerView.adapter as HomeRecyclerViewAdapter).getItems()[0].isEnRoute =
                            true
                        tasksRecyclerView.adapter?.notifyDataSetChanged()
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