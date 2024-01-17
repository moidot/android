package com.moidot.moidot.data.local.entity

import androidx.room.Entity

@Entity(tableName = "places")
data class PlaceEntity(
    val placeName: String,
    val roadAddressName: String?,
    val longitude: Double,
    val latitude: Double,
)