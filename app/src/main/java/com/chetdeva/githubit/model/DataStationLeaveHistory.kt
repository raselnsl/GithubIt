package com.chetdeva.githubit.model


import com.google.gson.annotations.SerializedName

data class DataStationLeaveHistory(
    @SerializedName("approval_end_date")
    val approvalEndDate: String?,
    @SerializedName("approval_from_date")
    val approvalFromDate: String?,
    @SerializedName("approval_head_id")
    val approvalHeadId: Any?,
    @SerializedName("approval_total_days")
    val approvalTotalDays: String?,
    @SerializedName("body")
    val body: String?,
    @SerializedName("country_id")
    val countryId: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("district_id")
    val districtId: String?,
    @SerializedName("file")
    val `file`: Any?,
    @SerializedName("from_date")
    val fromDate: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("leave_approval")
    val leaveApproval: List<LeaveApproval?>?,
    @SerializedName("leave_head_id")
    val leaveHeadId: Any?,
    @SerializedName("leave_time_location")
    val leaveTimeLocation: String?,
    @SerializedName("LeaveType")
    val leaveType: Any?,
    @SerializedName("medical_leave")
    val medicalLeave: String?,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("ref_contact_no")
    val refContactNo: String?,
    @SerializedName("ref_no")
    val refNo: String?,
    @SerializedName("state_id")
    val stateId: String?,
    @SerializedName("stationary")
    val stationary: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("to_date")
    val toDate: String?,
    @SerializedName("total_days")
    val totalDays: String?,
    @SerializedName("upazila_id")
    val upazilaId: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("UserName")
    val userName: String?
)