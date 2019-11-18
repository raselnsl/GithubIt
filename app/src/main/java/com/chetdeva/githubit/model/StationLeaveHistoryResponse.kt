package com.chetdeva.githubit.model


import com.google.gson.annotations.SerializedName

data class StationLeaveHistoryResponse(
    @SerializedName("issuccessful")
    val issuccessful: Boolean?,
    @SerializedName("0")
    val zeroStationLeaveHistory: ZeroStationLeaveHistory?
)