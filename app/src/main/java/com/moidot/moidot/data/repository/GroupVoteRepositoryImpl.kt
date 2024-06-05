package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupVoteRemoteDataSource
import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseUsersVotePlaceInfo
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.repository.GroupVoteRepository
import javax.inject.Inject

class GroupVoteRepositoryImpl @Inject constructor(private val groupVoteRemoteDataSource: GroupVoteRemoteDataSource) : GroupVoteRepository {
    override suspend fun getVoteStatus(groupId: Int, userId: Int): Result<ResponseVoteStatus> {
        return groupVoteRemoteDataSource.getVoteStatus(groupId, userId)
    }

    override suspend fun createVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote> {
        return groupVoteRemoteDataSource.createVote(groupId, requestCreateVote)
    }

    override suspend fun reCreateVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote> {
        return groupVoteRemoteDataSource.reCreateVote(groupId, requestCreateVote)
    }

    override suspend fun endVote(groupId: Int): Result<BaseResponse> {
        return groupVoteRemoteDataSource.endVote(groupId)
    }

    override suspend fun votePlace(groupId: Int, bestPlaceIds: List<Int>): Result<BaseResponse> {
        return groupVoteRemoteDataSource.votePlace(groupId, bestPlaceIds)
    }

    override suspend fun getUsersPlaceVoteInfo(groupId: Int, bestPlaceId: Int): Result<ResponseUsersVotePlaceInfo> {
        return groupVoteRemoteDataSource.getVotePlaceUsersInfo(groupId, bestPlaceId)
    }
}