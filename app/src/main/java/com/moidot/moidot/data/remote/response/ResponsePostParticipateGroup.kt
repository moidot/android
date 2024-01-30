package com.moidot.moidot.data.remote.response

data class ResponsePostParticipateGroup(
    val data: Data,
): BaseResponse() {
    data class Data(
        val groupId: Int,
        val latitude: Double,
        val locationName: String,
        val longitude: Double,
        val participationId: Int,
        val transportation: String,
        val userId: Int,
        val userName: String
    )
}