package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupVoteRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.repository.GroupVoteRepository
import javax.inject.Inject

class GroupVoteRepositoryImpl @Inject constructor(private val groupVoteRemoteDataSource: GroupVoteRemoteDataSource) : GroupVoteRepository {
    override suspend fun getVoteStatus(groupId: Int): Result<ResponseVoteStatus> {
        return groupVoteRemoteDataSource.getVoteStatus(groupId)
    }
}