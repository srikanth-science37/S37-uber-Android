package com.mp.poc.s37uberandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mp.poc.s37uberandroid.R
import com.mp.poc.s37uberandroid.model.HomeRecyclerViewModel

class TodaySchedulesRVAdapter(
    private val mList: List<HomeRecyclerViewModel>,
    private val itemClickListener: OnHomeRecyclerItemClickListener
) :
    RecyclerView.Adapter<TodaySchedulesRVAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_tasks_list_item, parent, false)

        return ViewHolder(view, itemClickListener)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        if (itemsViewModel.isEnRoute) {
            holder.futureTaskContainer.visibility = View.GONE
            holder.screeningTaskContainer.visibility = View.VISIBLE
            holder.screeningTaskTextView.text = itemsViewModel.taskText
            holder.screeningTaskEpochTextView.text = itemsViewModel.epochText
        } else {
            holder.screeningTaskContainer.visibility = View.GONE
            holder.futureTaskContainer.visibility = View.VISIBLE
            holder.futureTaskTextView.text = itemsViewModel.taskText
            holder.futureTaskEpochTextView.text = itemsViewModel.epochText
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    fun getItems(): List<HomeRecyclerViewModel> = mList

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View, listener: OnHomeRecyclerItemClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val screeningTaskContainer: ConstraintLayout =
            itemView.findViewById(R.id.enRouteTaskContainer)
        val screeningTaskTextView: TextView = itemView.findViewById(R.id.tvScreening)
        val screeningTaskEpochTextView: TextView = itemView.findViewById(R.id.tvScreeningEpoch)
        val futureTaskContainer: ConstraintLayout = itemView.findViewById(R.id.futureTaskContainer)
        val futureTaskTextView: TextView = itemView.findViewById(R.id.tvFutureTask)
        val futureTaskEpochTextView: TextView = itemView.findViewById(R.id.tvFutureTaskEpoch)
        private val clickListener: OnHomeRecyclerItemClickListener

        init {
            itemView.setOnClickListener(this)
            clickListener = listener
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(adapterPosition)
        }
    }

    interface OnHomeRecyclerItemClickListener {
        fun onItemClick(itemIndex: Int)
    }
}