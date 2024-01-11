package com.moidot.moidot.data.remote.response

data class ResponseRefreshToken(
    val data: Data
) : BaseResponse() {
    data class Data(
        val accessToken: String
    )
}