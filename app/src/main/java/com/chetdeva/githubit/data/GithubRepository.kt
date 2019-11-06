package com.chetdeva.githubit.data

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.annotation.MainThread
import com.chetdeva.githubit.api.GithubApiService
import com.chetdeva.githubit.api.Item
import java.util.concurrent.Executor

class GithubRepository(
        private val githubApi: GithubApiService,
        private val networkExecutor: Executor
) {

    @MainThread
    fun searchUsers(searchQuery: String, pageSize: Int): Listing<Item> {

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
        return GithubDataSourceFactory(searchQuery, githubApi, networkExecutor)
    }

    private fun pagedListConfig(pageSize: Int): PagedList.Config {
        return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPageSize(pageSize)
                .build()
    }
}

