package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.request.RequestPostParticipateGroup
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseCheckNicknameDuplication
import com.moidot.moidot.data.remote.response.ResponseGroupUserInfo
import com.moidot.moidot.data.remote.response.ResponsePostParticipateGroup
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupService {

    @GET("/group/participate")
    suspend fun getParticipateGroup(): Response<ResponseParticipateGroup>

    @POST("/group/participate")
    suspend fun participateGroup(@Body requestPostParticipateGroup: RequestPostParticipateGroup): Response<ResponsePostParticipateGroup>

    @POST("/group")
    suspend fun createGroup(@Body requestCreateGroup: RequestCreateGroup): Response<ResponseCreateGroup>

    @GET("/group/nickname")
    suspend fun checkNicknameDuplication(@Query("groupId") groupId: Int, @Query("nickname") nickname: String): Response<ResponseCheckNicknameDuplication>

    @GET("/group/user")
    suspend fun getUserInfo(@Query("groupId") groupId: Int): Response<ResponseGroupUserInfo>

    @GET("/group/participate")
    suspend fun getFilteredGroupList(@Query("spaceName") query:String, @Query("filter") filter:String): Response<ResponseParticipateGroup>

    @DELETE("/group/participate")
    suspend fun deleteGroup(@Query("participateId") participateId: Int) : Response<BaseResponse>
}