package com.moidot.moidot.data.remote.response

data class ResponseGroupUserInfo(
    val `data`: Data,
) : BaseResponse() {
    data class Data(
        val isAdmin: Boolean,
        val locationName: String,
        val participationId: Int,
        val latitude: Double,
        val longitude: Double,
        val transportation: String,
        val userEmail: String,
        val userName: String
    )
}