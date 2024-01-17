package com.moidot.moidot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    val placeName: String,
    val roadAddressName: String?,
    val longitude: Double,
    val latitude: Double,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
