package com.moidot.moidot.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseSearchPlace(
    @SerializedName("documents") val places: List<Document>,
) {
    data class Document(
        @SerializedName("place_name") val placeName: String,
        @SerializedName("road_address_name") val roadAddressName: String?,
        @SerializedName("x") val longitude: Double,
        @SerializedName("y") val latitude: Double
    )
}