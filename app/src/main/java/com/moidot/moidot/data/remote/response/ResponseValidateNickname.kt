package com.moidot.moidot.data.remote.response

data class ResponseCheckNicknameDuplication(
    val `data`: Data,
) : BaseResponse() {
    data class Data(
        val duplicated: Boolean
    )
}