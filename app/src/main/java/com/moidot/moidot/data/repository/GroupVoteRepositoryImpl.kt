package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupVoteRemoteDataSource
import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.repository.GroupVoteRepository
import javax.inject.Inject

class GroupVoteRepositoryImpl @Inject constructor(private val groupVoteRemoteDataSource: GroupVoteRemoteDataSource) : GroupVoteRepository {
    override suspend fun getVoteStatus(groupId: Int): Result<ResponseVoteStatus> {
        return groupVoteRemoteDataSource.getVoteStatus(groupId)
    }

    override suspend fun createVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote> {
        return groupVoteRemoteDataSource.createVote(groupId, requestCreateVote)
    }

    override suspend fun reCreateVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote> {
        return groupVoteRemoteDataSource.reCreateVote(groupId, requestCreateVote)
    }

    override suspend fun endVote(groupId: Int): Result<BaseResponse> {
        return  groupVoteRemoteDataSource.endVote(groupId)
    }
}