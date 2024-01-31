package com.moidot.moidot.data.remote.datasource

import retrofit2.Response

suspend fun <T> Response<T>.getResultFromResponse(): Result<T> {
    return if (this.isSuccessful) {
        this.body()?.let { Result.success(it) } ?: Result.failure(IllegalStateException("네트워크 통신 오류"))
    } else {
        // this.message()
        Result.failure(IllegalStateException("네트워크 통신 오류"))
    }
}
