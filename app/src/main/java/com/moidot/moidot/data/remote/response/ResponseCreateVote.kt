package com.moidot.moidot.data.remote.response

data class ResponseCreateVote(
    val `data`: Data,
) : BaseResponse() {
    data class Data(
        val endAt: String,
        val groupId: Int,
        val isAnonymous: Boolean,
        val isClosed: Boolean,
        val isEnabledMultipleChoice: Boolean,
        val voteId: Int
    )
}