package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseVoteStatus

interface GroupVoteRemoteDataSource {
    suspend fun getVoteStatus(groupId:Int): Result<ResponseVoteStatus>

    suspend fun createVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote>
}