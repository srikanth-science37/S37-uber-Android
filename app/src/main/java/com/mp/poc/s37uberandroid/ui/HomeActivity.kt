package com.mp.poc.s37uberandroid.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.model.HomeRecyclerViewModel
import com.mp.poc.s37uberandroid.ui.adapter.HomeRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeRecyclerViewAdapter.OnHomeRecyclerItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val data = mutableListOf<HomeRecyclerViewModel>()

        for (i in 1..3) {
            val model =
                HomeRecyclerViewModel(
                    "Screening Visit $i", "Oct 01, 2021 - $i:00 PM", i == 1
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

    override fun onItemClick(itemIndex: Int) {
        if ((tasksRecyclerView.adapter as HomeRecyclerViewAdapter).getItems()[itemIndex].isEnRoute)
            goToDetailScreen()
        else
            Toast.makeText(this, "Item ${itemIndex + 1} clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun goToDetailScreen() {
        startActivity(Intent(this, ScreeningInfoActivity::class.java))
    }
}