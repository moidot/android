package com.moidot.moidot.data.remote.response

data class ResponseSignIn(
    val data: Data,
) : BaseResponse() {
    data class Data(
        val accessToken: String,
        val email: String,
        val name: String,
        val refreshToken: String,
        val userId: Int
    )
}