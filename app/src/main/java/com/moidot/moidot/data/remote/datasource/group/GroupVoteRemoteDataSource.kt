package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.response.ResponseVoteStatus

interface GroupVoteRemoteDataSource {
    suspend fun getVoteStatus(groupId:Int): Result<ResponseVoteStatus>
}