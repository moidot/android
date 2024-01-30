package com.moidot.moidot.data.remote.request

data class RequestPostParticipateGroup(
    val groupId: Int,
    val latitude: Double,
    val locationName: String,
    val longitude: Double,
    val transportationType: String,
    val userName: String
)