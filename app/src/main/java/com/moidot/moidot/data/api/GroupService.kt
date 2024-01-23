package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GroupService {

    @GET("/group/participate")
    suspend fun getParticipateGroup(): Response<ResponseParticipateGroup>

    @POST("/group")
    suspend fun createGroup(@Body requestCreateGroup: RequestCreateGroup): Response<ResponseCreateGroup>
}