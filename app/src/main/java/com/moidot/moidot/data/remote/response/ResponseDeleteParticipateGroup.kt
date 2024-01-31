package com.moidot.moidot.data.remote.response

data class ResponseDeleteParticipateGroup(
    val `data`: Data,
):BaseResponse() {
    data class Data(
        val isDeletedSpace: Boolean,
        val message: String
    )
}