package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseBestRegion
import retrofit2.Response
import retrofit2.http.GET

interface GroupPlaceService {
    @GET("/group/best-region")
    suspend fun getBestRegions(groupId: Int): Response<ResponseBestRegion>
}