package com.moidot.moidot.data.remote.datasource

import com.google.gson.Gson
import com.moidot.moidot.data.remote.response.ResponseError
import retrofit2.Response

suspend fun <T> Response<T>.getResultFromResponse(): Result<T> {
    return if (this.isSuccessful) {
        this.body()?.let { Result.success(it) } ?: Result.failure(IllegalStateException("네트워크 통신 오류"))
    } else {
        val errorResponse = getErrorResponse(this.errorBody()?.string()!!)
        Result.failure(IllegalStateException(errorResponse.message))
    }
}

fun getErrorResponse(errorBody: String): ResponseError {
    return Gson().fromJson(errorBody, ResponseError::class.java)
}
