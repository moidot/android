package com.moidot.moidot.data.local

import com.moidot.moidot.data.local.entity.PlaceEntity
import com.moidot.moidot.data.remote.response.ResponseCoorToAddress
import com.moidot.moidot.data.remote.response.ResponseSearchPlace

fun PlaceEntity.toDocument(): ResponseSearchPlace.Document {
    return ResponseSearchPlace.Document(
        placeName = this.placeName,
        addressName = this.addressName,
        roadAddressName = this.roadAddressName,
        longitude = this.longitude,
        latitude = this.latitude,
        isFavorite = this.isFavorite
    )
}

fun ResponseSearchPlace.Document.toPlaceEntity(): PlaceEntity {
    return PlaceEntity(
        placeName = this.placeName,
        addressName = this.addressName,
        roadAddressName = this.roadAddressName,
        longitude = this.longitude,
        latitude = this.latitude,
        isFavorite = this.isFavorite
    )
}

fun ResponseCoorToAddress.Document.toDocument(longitude: Double, latitude: Double): ResponseSearchPlace.Document {
    return ResponseSearchPlace.Document(
        placeName = this.address.addressName,
        addressName = this.address.addressName,
        roadAddressName = this.roadAddress?.roadAddressName,
        longitude = longitude,
        latitude = latitude,
        isFavorite = false
    )
}