package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseUsersVotePlaceInfo
import com.moidot.moidot.data.remote.response.ResponseVoteStatus

interface GroupVoteRemoteDataSource {
    suspend fun getVoteStatus(groupId: Int, userId: Int): Result<ResponseVoteStatus>

    suspend fun createVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote>

    suspend fun reCreateVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote>

    suspend fun endVote(groupId: Int): Result<BaseResponse>

    suspend fun votePlace(groupId: Int, bestPlaceIds: List<Int>): Result<BaseResponse>

    suspend fun getVotePlaceUsersInfo(groupId: Int, bestPlaceId:Int): Result<ResponseUsersVotePlaceInfo>
}
