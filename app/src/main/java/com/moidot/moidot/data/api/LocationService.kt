package com.moidot.moidot.data.api

import com.moidot.moidot.BuildConfig.KAKAO_REST_API_KEY
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LocationService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlace(
        @Header("Authorization") authorization: String = KAKAO_REST_API_KEY,
        @Query("query") query: String
    ): Response<ResponseSearchPlace>

}