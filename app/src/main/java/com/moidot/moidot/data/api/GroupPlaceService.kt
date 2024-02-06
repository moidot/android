package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseBestRegion
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupPlaceService {
    @GET("/group/best-region")
    suspend fun getBestRegions(@Query("groupId") groupId: Int): Response<ResponseBestRegion>
}