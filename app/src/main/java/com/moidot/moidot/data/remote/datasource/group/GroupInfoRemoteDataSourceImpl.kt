package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupInfoService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseDeleteParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import javax.inject.Inject

class GroupInfoRemoteDataSourceImpl @Inject constructor(private val groupInfoService: GroupInfoService) : GroupInfoRemoteDataSource {
    override suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo> {
        return groupInfoService.getGroupInfo(groupId).getResultFromResponse()
    }

    override suspend fun deleteGroup(groupId: Int): Result<BaseResponse> {
        return groupInfoService.deleteGroup(groupId).getResultFromResponse()
    }

    override suspend fun deleteParticipateGroup(participateId: Int): Result<ResponseDeleteParticipateGroup> {
       return groupInfoService.deleteParticipateGroup(participateId).getResultFromResponse()
    }

    override suspend fun removeMember(participateId: Int): Result<BaseResponse> {
        return groupInfoService.removeMember(participateId).getResultFromResponse()
    }

    override suspend fun editGroupName(groupId: Int, groupName: String): Result<BaseResponse> {
        return groupInfoService.editGroupName(groupId, groupName).getResultFromResponse()
    }
}