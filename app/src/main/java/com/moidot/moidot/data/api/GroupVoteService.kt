package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseUsersVotePlaceInfo
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupVoteService {

    // 투표 현황 조회
    @GET("/group/{groupId}/vote")
    suspend fun getVoteStatus(@Path("groupId") groupId: Int, @Query("user") userId: String): Response<ResponseVoteStatus>

    @POST("/group/{groupId}/vote")
    suspend fun createVote(@Path("groupId") groupId: Int, @Body requestCreateVote: RequestCreateVote): Response<ResponseCreateVote>

    @POST("/group/{spaceId}/vote")
    suspend fun reCreateVote(@Path("spaceId") groupId: Int, @Body requestCreateVote: RequestCreateVote): Response<ResponseCreateVote>

    @PATCH("/group/{groupId}/vote")
    suspend fun endVote(@Path("groupId") groupId: Int): Response<ResponseCreateVote>

    @POST("/group/{groupId}/vote/select")
    suspend fun votePlace(@Path("groupId") groupId: Int, @Query("bestPlaceIds") bestPlaceIds: List<Int>): Response<BaseResponse>

    @POST("/group/{groupId}/vote/select")
    suspend fun votePlace(@Path("groupId") groupId: Int, @Query("bestPlaceIds") bestPlaceIds: String): Response<BaseResponse>

    // 장소에 투표한 인원 조회
    @GET("/group/{groupId}/vote/select")
    suspend fun getVotePlaceUsersInfo(@Path("groupId") groupId: Int, @Query("bestPlaceId") bestPlaceId: Int): Response<ResponseUsersVotePlaceInfo>

}