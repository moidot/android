package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupVoteService {

    @GET("/group/{groupId}/vote")
    suspend fun getVoteStatus(@Path("groupId") groupId: Int): Response<ResponseVoteStatus>

    @POST("/group/{groupId}/vote")
    suspend fun createVote(@Path("groupId") groupId: Int, @Body requestCreateVote: RequestCreateVote): Response<ResponseCreateVote>

    @POST("/group/{spaceId}/vote")
    suspend fun reCreateVote(@Path("spaceId") groupId: Int, @Body requestCreateVote: RequestCreateVote): Response<ResponseCreateVote>

    @PATCH("/group/{groupId}/vote")
    suspend fun endVote(@Path("groupId") groupId: Int): Response<ResponseCreateVote>
}