package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseDeleteParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupInfoService {

    @GET("/group")
    suspend fun getGroupInfo(@Query("groupId") groupId: Int): Response<ResponseGroupInfo>

    @DELETE("/group/participate")
    suspend fun deleteParticipateGroup(@Body participateId: String): Response<ResponseDeleteParticipateGroup>
}