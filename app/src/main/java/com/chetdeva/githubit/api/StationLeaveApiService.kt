package com.chetdeva.githubit.api

import com.chetdeva.githubit.model.StationLeaveHistoryResponse

class StationLeaveApiService(
        private val stationLeaveApi: StationLeaveApi
) {
    fun searchUsersSync(
            query: String, page: Int, perPage: Int,
            onPrepared: () -> Unit,
            onSuccess: (StationLeaveHistoryResponse?) -> Unit,
            onError: (String) -> Unit
    ) {
        val request = stationLeaveApi.searchUsers(query, page)
        onPrepared()
        ApiRequestHelper.syncRequest(request, onSuccess, onError)
    }
    fun searchUsersAsync(
            query: String, page: Int, perPage: Int,
            onPrepared: () -> Unit,
            onSuccess: (StationLeaveHistoryResponse?) -> Unit,
            onError: (String) -> Unit
    ) {
        val request = stationLeaveApi.searchUsers(query, page)
        onPrepared()
        ApiRequestHelper.asyncRequest(request, onSuccess, onError)
    }
}
