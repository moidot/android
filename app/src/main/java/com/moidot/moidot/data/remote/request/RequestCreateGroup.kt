package com.moidot.moidot.data.remote.request

data class RequestCreateGroup(
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val name: String,
    val transportationType: String,
    val userName: String
)