package com.moidot.moidot.data.remote.request

data class RequestCreateGroup(
    val latitude: Double,
    val locationName: String,
    val longitude: Double,
    val name: String,
    val transportationType: String,
    val userName: String
)