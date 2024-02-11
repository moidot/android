package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupVoteService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import javax.inject.Inject

class GroupVoteRemoteDataSourceImpl @Inject constructor(private val groupVoteService: GroupVoteService) : GroupVoteRemoteDataSource {
    override suspend fun getVoteStatus(groupId: Int): Result<ResponseVoteStatus> {
        return groupVoteService.getVoteStatus(groupId).getResultFromResponse()
    }
}