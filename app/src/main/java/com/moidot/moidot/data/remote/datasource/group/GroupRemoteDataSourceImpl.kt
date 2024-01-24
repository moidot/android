package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseValidateNickname
import javax.inject.Inject

class GroupRemoteDataSourceImpl @Inject constructor(private val groupService: GroupService) : GroupRemoteDataSource {
    override suspend fun getMyGroupList(): Result<ResponseParticipateGroup> {
        return groupService.getParticipateGroup().getResultFromResponse()
    }

    override suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup> {
        return groupService.createGroup(requestCreateGroup).getResultFromResponse()
    }

    override suspend fun validateNickname(groupId: Int, nickname: String): Result<ResponseValidateNickname> {
        return groupService.validateNickname(groupId, nickname).getResultFromResponse()
    }
}