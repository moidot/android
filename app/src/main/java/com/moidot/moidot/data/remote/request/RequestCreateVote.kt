package com.moidot.moidot.data.remote.request

data class RequestCreateVote(
    val isAnonymous: Boolean,
    val isEnabledMultipleChoice: Boolean,
    val endAt: String,
)