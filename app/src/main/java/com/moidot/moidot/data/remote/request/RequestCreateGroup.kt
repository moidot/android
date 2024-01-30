package com.moidot.moidot.data.remote.request

data class RequestCreateGroup(
    val date: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val transportationType: String,
    val userName: String
)