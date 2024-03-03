package com.moidot.moidot.repository

import com.moidot.moidot.data.remote.request.RequestCreateVote
import com.moidot.moidot.data.remote.response.ResponseCreateVote
import com.moidot.moidot.data.remote.response.ResponseVoteStatus

interface GroupVoteRepository {
    suspend fun getVoteStatus(groupId: Int): Result<ResponseVoteStatus>

    suspend fun createVote(groupId: Int, requestCreateVote: RequestCreateVote): Result<ResponseCreateVote>
}