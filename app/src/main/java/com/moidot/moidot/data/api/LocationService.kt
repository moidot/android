package com.moidot.moidot.data.api

import com.moidot.moidot.BuildConfig.KAKAO_REST_API_KEY
import com.moidot.moidot.data.remote.response.ResponseCoorToAddress
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LocationService {
    @GET("v2/local/search/keyword.json")
    suspend fun getSearchPlace(
        @Header("Authorization") authorization: String = "KakaoAK $KAKAO_REST_API_KEY",
        @Query("query") query: String
    ): Response<ResponseSearchPlace>

    @GET("v2/local/geo/coord2address.json")
    suspend fun getCoordToAddress(
        @Header("Authorization") authorization: String = "KakaoAK $KAKAO_REST_API_KEY",
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
    ): Response<ResponseCoorToAddress>

}