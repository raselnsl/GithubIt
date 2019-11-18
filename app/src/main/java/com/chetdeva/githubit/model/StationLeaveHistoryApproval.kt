package com.chetdeva.githubit.model


import com.google.gson.annotations.SerializedName

data class StationLeaveHistoryApproval(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("from_date")
    val fromDate: String?,
    @SerializedName("from_user_id")
    val fromUserId: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("leave_approval_id")
    val leaveApprovalId: String?,
    @SerializedName("leave_head_id")
    val leaveHeadId: String?,
    @SerializedName("leave_id")
    val leaveId: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("other_reliever")
    val otherReliever: String?,
    @SerializedName("other_reliever_user_id")
    val otherRelieverUserId: String?,
    @SerializedName("reliever_designation")
    val relieverDesignation: String?,
    @SerializedName("reliever_name")
    val relieverName: String?,
    @SerializedName("reliever_user_id")
    val relieverUserId: String?,
    @SerializedName("seen")
    val seen: String?,
    @SerializedName("stationary")
    val stationary: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("to_date")
    val toDate: String?,
    @SerializedName("to_user_id")
    val toUserId: String?,
    @SerializedName("total_days")
    val totalDays: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)