package com.moidot.moidot.repository

import com.moidot.moidot.data.remote.response.ResponseVoteStatus

interface GroupVoteRepository {
    suspend fun getVoteStatus(groupId: Int): Result<ResponseVoteStatus>
}