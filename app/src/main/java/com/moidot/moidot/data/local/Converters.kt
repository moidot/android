package com.moidot.moidot.data.local

import com.moidot.moidot.data.local.entity.PlaceEntity
import com.moidot.moidot.data.remote.response.ResponseSearchPlace

fun PlaceEntity.toDocument(): ResponseSearchPlace.Document {
    return ResponseSearchPlace.Document(
        placeName = this.placeName,
        roadAddressName = this.roadAddressName,
        longitude = this.longitude,
        latitude = this.latitude,
        isFavorite = this.isFavorite
    )
}

fun ResponseSearchPlace.Document.toPlaceEntity(): PlaceEntity {
    return PlaceEntity(
        placeName = this.placeName,
        roadAddressName = this.roadAddressName,
        longitude = this.longitude,
        latitude = this.latitude,
        isFavorite = this.isFavorite
    )
}