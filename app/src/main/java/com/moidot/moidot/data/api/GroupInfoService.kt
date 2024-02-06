package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseDeleteParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface GroupInfoService {

    @GET("/group")
    suspend fun getGroupInfo(@Query("groupId") groupId: Int): Response<ResponseGroupInfo>

    @DELETE("/group")
    suspend fun deleteGroup(@Query("groupId") groupId: Int): Response<BaseResponse>

    @DELETE("/group/participate/removal")
    suspend fun removeMember(@Query("participateId") participateId: Int): Response<BaseResponse>

    @DELETE("/group/participate")
    suspend fun deleteParticipateGroup(@Query("participateId") participateId: Int): Response<ResponseDeleteParticipateGroup>

    @PATCH("/group")
    suspend fun editGroupName(@Query("groupId") groupId: Int, @Body groupName: String): Response<BaseResponse>
}