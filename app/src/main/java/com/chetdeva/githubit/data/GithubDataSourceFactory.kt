package com.chetdeva.githubit.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.chetdeva.githubit.api.GithubApi
import com.chetdeva.githubit.api.Item
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class GithubDataSourceFactory(
        private val githubApi: GithubApi,
        private val searchQuery: String,
        private val retryExecutor: Executor) : DataSource.Factory<String, Item>() {
    val sourceLiveData = MutableLiveData<PageKeyedGithubDataSource>()
    override fun create(): DataSource<String, Item> {
        val source = PageKeyedGithubDataSource(githubApi, searchQuery, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
