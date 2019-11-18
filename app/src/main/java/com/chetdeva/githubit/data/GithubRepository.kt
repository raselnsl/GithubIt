package com.chetdeva.githubit.data

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.chetdeva.githubit.api.StationLeaveApiService
import com.chetdeva.githubit.model.DataStationLeaveHistory
import java.util.concurrent.Executor

class GithubRepository(
        private val stationLeaveApiService: StationLeaveApiService,
        private val networkExecutor: Executor
) {

    @MainThread
    fun searchUsers(searchQuery: String, pageSize: Int): Listing<DataStationLeaveHistory> {

        val factory = githubDataSourceFactory(searchQuery)

        val config = pagedListConfig(pageSize)

        val livePagedList = LivePagedListBuilder(factory, config)
                .setFetchExecutor(networkExecutor)
                .build()

        return Listing(
                pagedList = livePagedList,
                networkState = switchMap(factory.source) { it.network },
                retry = { factory.source.value?.retryAllFailed() },
                refresh = { factory.source.value?.invalidate() },
                refreshState = switchMap(factory.source) { it.initial })
    }

    private fun githubDataSourceFactory(searchQuery: String): GithubDataSourceFactory {
        return GithubDataSourceFactory(searchQuery, stationLeaveApiService, networkExecutor)
    }

    private fun pagedListConfig(pageSize: Int): PagedList.Config {
        return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPageSize(pageSize)
                .build()
    }
}

