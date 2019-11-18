package com.chetdeva.githubit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chetdeva.githubit.R
import com.chetdeva.githubit.data.NetworkState
import com.chetdeva.githubit.model.DataStationLeaveHistory
import com.chetdeva.githubit.ui.UsersAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()

    private lateinit var homeViewModel: HomeViewModel

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
        val adapter = UsersAdapter() {
            homeViewModel.retry()
        }
        list.adapter = adapter
        homeViewModel.items.observe(this, Observer<PagedList<DataStationLeaveHistory>> {
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
        const val tokenValue = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQ0NzAyZGQyMmFlYTJjMTZiZmVhYTg4ODk3Njg4MmZlZjc4M2IyNDA5NzlkN2Y2Y2RhNTNiNTAyYWNlZjAyMjA4N2NmMGM4ZjJjOWZmNzJhIn0.eyJhdWQiOiIxIiwianRpIjoiZDQ3MDJkZDIyYWVhMmMxNmJmZWFhODg4OTc2ODgyZmVmNzgzYjI0MDk3OWQ3ZjZjZGE1M2I1MDJhY2VmMDIyMDg3Y2YwYzhmMmM5ZmY3MmEiLCJpYXQiOjE1NzM5ODU5OTIsIm5iZiI6MTU3Mzk4NTk5MiwiZXhwIjoxNjA1NjA4MzkyLCJzdWIiOiI2MSIsInNjb3BlcyI6W119.bPvsHnxGF35WuHFIQGRSPaoo_I9KQBKzTucYoN-kJ-6VVd7oLCiFXYbkPG7ZhovM1k6vycP5LnQTfVr96Gjx39GciSH12y1uBn0MzlZEhLykO8bClU3HT8ARrrlQ7S9vbTraaKIln9AnfS66RE6FW1usRJFfyxeaFl2AVv4fSIbTCfN2aWiPeGTLqQcxH3egFyMbEFVaqaaZoS7bMFYFpqvzOJMWRJoclY-_iNNpxr7nDhFBpVi-ksQXPP_QZ78dH2d6slZtmZbpHKY8uPfU2sn3QrZlJmEgRyeNLKVXX57GKNU9m5PuGuV5A9OdV-tBxtEnuSdvekTrREltIeaJmxDunCyiRvprVf59eYQnSO19Myg2jM0gC7Zoo4XGPLeYJUZe_7t0BAU6g7Wvl7ylISxbA_NOtNwAr8WzT13Fb3jnI6Akcx5MzPa5Yl0z9pge19W_PdmwQUXX4uzNyF7Keh7_xt8g8MdnKfA22h0fhS8MirU1cwWOUESARZ4D4sm1UAQfR2Y4C869PPVV_QDG6TdiDflpq7j8NTYJF8N6dp0wn4PRUNjHkV8dUkhBvwOZ5Q8X46riRDCp5D7UMVwoCgAfYE0WNT2JRK6tEgWx6izcvhSbzG-Awykga5OeFpGuUBJ3aOF8fKaZia1bPLJYmUcidWzQZdcfYjDXKlX1Nqs"
    }
}