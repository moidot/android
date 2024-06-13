package com.moidot.moidot.data.remote.request

data class RequestEditGroupInfo(
    val latitude: Double,
    val locationName: String,
    val longitude: Double,
    val participateId: Int,
    val transportationType: String,
    val userName: String
)