package com.chetdeva.githubit.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.chetdeva.githubit.api.StationLeaveApiService
import com.chetdeva.githubit.model.DataStationLeaveHistory
import java.util.concurrent.Executor

class GithubDataSourceFactory(
        private val searchQuery: String,
        private val stationLeaveApiService: StationLeaveApiService,
        private val retryExecutor: Executor
) : DataSource.Factory<Int, DataStationLeaveHistory>() {

    val source = MutableLiveData<GithubPageKeyedDataSource>()

    override fun create(): DataSource<Int, DataStationLeaveHistory> {
        val source = GithubPageKeyedDataSource(searchQuery, stationLeaveApiService, retryExecutor)
        this.source.postValue(source)
        return source
    }
}
