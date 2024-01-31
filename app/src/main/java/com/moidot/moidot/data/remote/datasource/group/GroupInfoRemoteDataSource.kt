package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseDeleteParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseGroupInfo

interface GroupInfoRemoteDataSource {

    suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo>

    suspend fun deleteGroup(groupId: Int): Result<BaseResponse>

    suspend fun deleteParticipateGroup(participateId: Int): Result<ResponseDeleteParticipateGroup>
}