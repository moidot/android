package com.moidot.moidot.data.remote.response

data class ResponseError(
    val code: Int = -1,
    val message: String? = "네트워크 통신오류"
)