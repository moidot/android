package com.moidot.moidot.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseCoorToAddress(
    val documents: List<Document>,
) {
    data class Document(
        @SerializedName("address") val address: Address,
        @SerializedName("road_address") val roadAddress: RoadAddress?
    ) {
        data class RoadAddress(
            @SerializedName("address_name") val addressName: String,
        )

        data class Address(
            @SerializedName("address_name") val roadAddressName: String,
        )
    }
}