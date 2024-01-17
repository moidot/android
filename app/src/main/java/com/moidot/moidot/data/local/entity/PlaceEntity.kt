package com.moidot.moidot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val placeName: String,
    val roadAddressName: String?,
    val longitude: Double,
    val latitude: Double,
    var isFavorite: Boolean = true
)