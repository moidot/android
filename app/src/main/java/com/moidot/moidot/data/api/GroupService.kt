package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import retrofit2.Response
import retrofit2.http.GET

interface GroupService {

    @GET("/group/participate")
    suspend fun getParticipateGroup(): Response<ResponseParticipateGroup>
}