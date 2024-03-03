package com.moidot.moidot.data.remote.datasource

import android.util.Log
import com.google.gson.Gson
import com.moidot.moidot.data.remote.response.ResponseError
import okhttp3.ResponseBody
import retrofit2.Response

suspend fun <T> Response<T>.getResultFromResponse(): Result<T> {
    return if (this.isSuccessful) {
        this.body()?.let { Result.success(it) } ?: Result.failure(IllegalStateException("네트워크 통신 오류"))
    } else {
        if (checkNetworkError(errorBody())) return Result.failure(IllegalStateException("네트워크 통신 오류"))

        val errorResponse = getErrorResponse(this.errorBody()?.string()!!)
        Result.failure(IllegalStateException(errorResponse.message))
    }
}

// Vercel 오류 발생시 앱 터지는 현상 막기
private fun checkNetworkError(errorBody: ResponseBody?): Boolean {
    Log.e("error", "서버 환경설정 이슈")
    return errorBody?.string()?.contains("Vercel") ?: false
}

private fun getErrorResponse(errorBody: String): ResponseError {
    return Gson().fromJson(errorBody, ResponseError::class.java)
}
