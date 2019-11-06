package com.chetdeva.githubit.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chetdeva.githubit.R
import com.chetdeva.githubit.api.Item
import com.chetdeva.githubit.data.NetworkState
import com.chetdeva.githubit.ui.UsersAdapter
import com.chetdeva.githubit.util.GlideApp

import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()

    private lateinit var homeViewModel: HomeViewModel
    private val glideRequests by lazy { GlideApp.with(this) }
    private lateinit var list: RecyclerView
    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        list = root.findViewById(R.id.list)
        swipe_refresh = root.findViewById(R.id.swipe_refresh)
        initAdapter()
        initSwipeToRefresh()

        searchGithub()

        val searchQuery = savedInstanceState?.getString(token) ?: tokenValue
        homeViewModel.showSearchResults(searchQuery)
        return root
    }
    private fun initAdapter() {
        val adapter = UsersAdapter(glideRequests) {
            homeViewModel.retry()
        }
        list.adapter = adapter
        homeViewModel.items.observe(this, Observer<PagedList<Item>> {
            adapter.submitList(it)
        })
        homeViewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }
    private fun initSwipeToRefresh() {
        homeViewModel.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            homeViewModel.refresh()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(token, tokenValue)
    }
    private fun searchGithub() {
        tokenValue.trim().let {
            if (it.isNotEmpty()) {
                if (homeViewModel.showSearchResults(it)) {
                    list.scrollToPosition(0)
                    (list.adapter as? UsersAdapter)?.submitList(null)
                }
            }
        }
    }
    companion object {
        const val token = "token"
        const val tokenValue = "ios"
    }
}