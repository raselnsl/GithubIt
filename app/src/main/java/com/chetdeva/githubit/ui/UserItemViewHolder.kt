package com.chetdeva.githubit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chetdeva.githubit.R
import com.chetdeva.githubit.model.DataStationLeaveHistory
import com.google.android.material.textview.MaterialTextView


class UserItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvStatusValue: MaterialTextView = view.findViewById(R.id.tvStatusValue)
    private val tvFromDateValue: MaterialTextView = view.findViewById(R.id.tvFromDateValue)
    private val tvToDateValue: MaterialTextView = view.findViewById(R.id.tvToDateValue)
    private val tvTotalDays: MaterialTextView = view.findViewById(R.id.tvTotalDays)

    init {
        view.setOnClickListener {
//            item?.htmlUrl?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
        }
    }

    fun bind(data: DataStationLeaveHistory?) {
        data ?: return

        tvStatusValue.text = data.status ?: "N/A"
        tvFromDateValue.text = data.fromDate ?: "N/A"
        tvToDateValue.text = data.toDate ?: "N/A"
        tvTotalDays.text = data.totalDays ?: "N/A"
    }

    companion object {
        fun create(parent: ViewGroup): UserItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station_leave_history, parent, false)
            return UserItemViewHolder(view)
        }
    }
}