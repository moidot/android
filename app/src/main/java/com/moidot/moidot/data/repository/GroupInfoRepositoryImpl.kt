package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupInfoRemoteDataSource
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseDeleteParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.repository.GroupInfoRepository
import javax.inject.Inject

class GroupInfoRepositoryImpl @Inject constructor(private val groupInfoRemoteDataSource: GroupInfoRemoteDataSource) : GroupInfoRepository {
    override suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo> {
        return groupInfoRemoteDataSource.getGroupInfo(groupId)
    }

    override suspend fun deleteGroup(groupId: Int): Result<BaseResponse> {
        return groupInfoRemoteDataSource.deleteGroup(groupId)
    }

    override suspend fun removeMember(participateId: Int): Result<BaseResponse> {
        return groupInfoRemoteDataSource.removeMember(participateId)
    }

    override suspend fun deleteParticipateGroup(participateId: Int): Result<ResponseDeleteParticipateGroup> {
        return groupInfoRemoteDataSource.deleteParticipateGroup(participateId)
    }

    override suspend fun editGroupName(groupId: Int, groupName: String): Result<BaseResponse> {
       return groupInfoRemoteDataSource.editGroupName(groupId, groupName)
    }
}