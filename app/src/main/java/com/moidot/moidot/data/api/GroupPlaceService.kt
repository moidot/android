package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.data.remote.response.ResponseRecommendPlace
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupPlaceService {
    @GET("/group/best-region")
    suspend fun getBestRegions(@Query("groupId") groupId: Int): Response<ResponseBestRegion>

    @GET("/group/best-region/place")
    suspend fun getRecommendPlace(@Query("x") x: String, @Query("y") y: String, @Query("local") local: String, @Query("keyword") keyword: String): Response<ResponseRecommendPlace>
}