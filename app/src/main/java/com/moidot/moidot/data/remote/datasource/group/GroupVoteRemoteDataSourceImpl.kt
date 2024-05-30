package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupVoteService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import javax.inject.Inject

class GroupVoteRemoteDataSourceImpl @Inject constructor(private val groupVoteService: GroupVoteService) : GroupVoteRemoteDataSource {
    override suspend fun getVoteStatus(groupId: Int, userId:Int): Result<ResponseVoteStatus> {
        return groupVoteService.getVoteStatus(groupId, userId.toString()).getResultFromResponse()
    }

    override suspend fun createVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote> {
        return groupVoteService.createVote(groupId, requestCreateVote).getResultFromResponse()
    }

    override suspend fun reCreateVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote> {
        return groupVoteService.reCreateVote(groupId, requestCreateVote).getResultFromResponse()
    }

    override suspend fun endVote(groupId: Int): Result<BaseResponse> {
        return groupVoteService.endVote(groupId).getResultFromResponse()
    }

    override suspend fun votePlace(groupId: Int, bestPlaceIds: List<Int>): Result<BaseResponse> {
        return groupVoteService.votePlace(groupId, bestPlaceIds).getResultFromResponse()
    }
}