package com.moidot.moidot.data.remote.response

data class ResponseCreateGroup(
    val data: Data,
) : BaseResponse() {
    data class Data(
        val adminId: Int,
        val date: String,
        val fixedPlace: String,
        val groupId: Int,
        val name: String
    )
}