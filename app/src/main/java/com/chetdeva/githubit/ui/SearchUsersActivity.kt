package com.chetdeva.githubit.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.chetdeva.githubit.R
import com.chetdeva.githubit.api.Item
import com.chetdeva.githubit.data.NetworkState
import com.chetdeva.githubit.util.GlideApp
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.activity_search_repositories.*
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SearchUsersActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()

    companion object {
        const val KEY_GITHUB_USER = "github_user"
        const val DEFAULT_USER = "google"
    }

    private lateinit var list: RecyclerView
    private lateinit var model: SearchUsersViewModel
    private val glideRequests by lazy { GlideApp.with(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)

        setUpSsl()

        list = findViewById(R.id.list)

        model = viewModel()
        initAdapter()
        initSwipeToRefresh()
        val searchQuery = savedInstanceState?.getString(KEY_GITHUB_USER) ?: DEFAULT_USER
        model.showSearchResults(searchQuery)
    }

    private fun viewModel(): SearchUsersViewModel {
        return ViewModelProviders.of(this, factory).get(SearchUsersViewModel::class.java)
    }

    private fun initAdapter() {
        val adapter = UsersAdapter(glideRequests) {
            model.retry()
        }
        list.adapter = adapter
        model.items.observe(this, Observer<PagedList<Item>> {
            adapter.submitList(it)
        })
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        model.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_GITHUB_USER, model.currentSearchQuery())
    }


    private var searchView: SearchView? = null

    private var onQueryTextListener: SearchView.OnQueryTextListener? = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            searchGithub(query)
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            // do nothing
            return true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = searchView(menu)
        searchView?.queryHint = getString(R.string.search)
        searchView?.setOnQueryTextListener(onQueryTextListener)
        return true
    }

    private fun searchView(menu: Menu?): SearchView? {
        val searchItem = menu?.findItem(R.id.action_search)
        return searchItem?.actionView as? SearchView
    }
    private fun hideKeyboard() {
        if (searchView?.hasFocus() == true) searchView?.clearFocus()
    }

    private fun searchGithub(searchQuery: String) {
        searchQuery.trim().let {
            if (it.isNotEmpty()) {
                if (model.showSearchResults(it)) {
                    list.scrollToPosition(0)
                    (list.adapter as? UsersAdapter)?.submitList(null)
                    hideKeyboard()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        onQueryTextListener = null
        super.onDestroy()
    }
    private fun setUpSsl() {
        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

    }
}
