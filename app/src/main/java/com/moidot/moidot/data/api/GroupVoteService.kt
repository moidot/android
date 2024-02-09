package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupVoteService {

    @GET("/group/{groupId}/vote")
    suspend fun getVoteStatus(@Path("groupId") groupId: Int): Response<ResponseVoteStatus>

}