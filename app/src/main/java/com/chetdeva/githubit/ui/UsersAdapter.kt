package com.chetdeva.githubit.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chetdeva.githubit.R
import com.chetdeva.githubit.data.NetworkState
import com.chetdeva.githubit.model.DataStationLeaveHistory

class UsersAdapter(
        private val retryCallback: () -> Unit
) : PagedListAdapter<DataStationLeaveHistory, RecyclerView.ViewHolder>(ITEM_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_station_leave_history -> (holder as UserItemViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_station_leave_history -> UserItemViewHolder.create(parent)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.item_station_leave_history
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<DataStationLeaveHistory>() {
            override fun areItemsTheSame(oldItem: DataStationLeaveHistory, newItem: DataStationLeaveHistory): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DataStationLeaveHistory, newItem: DataStationLeaveHistory): Boolean =
                    oldItem == newItem
        }
    }
}