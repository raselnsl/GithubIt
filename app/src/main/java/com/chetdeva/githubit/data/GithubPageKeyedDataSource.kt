package com.chetdeva.githubit.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.chetdeva.githubit.api.StationLeaveApiService
import com.chetdeva.githubit.model.DataStationLeaveHistory
import java.util.concurrent.Executor

class GithubPageKeyedDataSource(
        private val searchQuery: String,
        private val stationLeaveApiService: StationLeaveApiService,
        private val retryExecutor: Executor
) : PageKeyedDataSource<Int, DataStationLeaveHistory>() {

    var retry: (() -> Any)? = null
    val network = MutableLiveData<NetworkState>()
    val initial = MutableLiveData<NetworkState>()

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataStationLeaveHistory>) { }
    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, DataStationLeaveHistory>) {

        val currentPage = 1
        val nextPage = currentPage + 1

        makeLoadInitialRequest(params, callback, currentPage, nextPage)
    }

    private fun makeLoadInitialRequest(params: LoadInitialParams<Int>,
                                       callback: LoadInitialCallback<Int, DataStationLeaveHistory>,
                                       currentPage: Int,
                                       nextPage: Int) {

        // triggered by a refresh, we better execute sync
        stationLeaveApiService.searchUsersSync(
                query = searchQuery,
                page = currentPage,
                perPage = params.requestedLoadSize,
                onPrepared = {
                    postInitialState(NetworkState.LOADING)
                },
                onSuccess = { responseBody ->
                    val items = responseBody?.zeroStationLeaveHistory?.data ?: emptyList()
                    retry = null
                    postInitialState(NetworkState.LOADED)
                    callback.onResult(items, null, nextPage)
                },
                onError = { errorMessage ->
                    retry = { loadInitial(params, callback) }
                    postInitialState(NetworkState.error(errorMessage))
                })
    }

    /**
     * load after
     */
    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, DataStationLeaveHistory>) {

        val currentPage = params.key
        val nextPage = currentPage + 1

        makeLoadAfterRequest(params, callback, currentPage, nextPage)
    }

    private fun makeLoadAfterRequest(params: LoadParams<Int>,
                                     callback: LoadCallback<Int, DataStationLeaveHistory>,
                                     currentPage: Int,
                                     nextPage: Int) {

        stationLeaveApiService.searchUsersAsync(
                query = searchQuery,
                page = currentPage,
                perPage = params.requestedLoadSize,
                onPrepared = {
                    postAfterState(NetworkState.LOADING)
                },
                onSuccess = { responseBody ->
                    val items = responseBody?.zeroStationLeaveHistory?.data ?: emptyList()
                    retry = null
                    callback.onResult(items, nextPage)
                    postAfterState(NetworkState.LOADED)
                },
                onError = { errorMessage ->
                    retry = { loadAfter(params, callback) }
                    postAfterState(NetworkState.error(errorMessage))
                })
    }

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let { retry ->
            retryExecutor.execute { retry() }
        }
    }

    private fun postInitialState(state: NetworkState) {
        network.postValue(state)
        initial.postValue(state)
    }

    private fun postAfterState(state: NetworkState) {
        network.postValue(state)
    }
}