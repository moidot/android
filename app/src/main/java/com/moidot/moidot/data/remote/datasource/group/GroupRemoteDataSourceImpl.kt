package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.request.RequestEditGroupInfo
import com.moidot.moidot.data.remote.request.RequestPostParticipateGroup
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseCheckNicknameDuplication
import com.moidot.moidot.data.remote.response.ResponseGroupUserInfo
import com.moidot.moidot.data.remote.response.ResponsePostParticipateGroup
import javax.inject.Inject

class GroupRemoteDataSourceImpl @Inject constructor(private val groupService: GroupService) : GroupRemoteDataSource {
    override suspend fun getMyGroupList(): Result<ResponseParticipateGroup> {
        return groupService.getParticipateGroup().getResultFromResponse()
    }

    override suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup> {
        return groupService.createGroup(requestCreateGroup).getResultFromResponse()
    }

    override suspend fun participateGroup(requestPostParticipateGroup: RequestPostParticipateGroup): Result<ResponsePostParticipateGroup> {
        return groupService.participateGroup(requestPostParticipateGroup).getResultFromResponse()
    }

    override suspend fun checkNicknameDuplication(groupId: Int, nickname: String): Result<ResponseCheckNicknameDuplication> {
        return groupService.checkNicknameDuplication(groupId, nickname).getResultFromResponse()
    }

    override suspend fun getUserInfo(groupId: Int): Result<ResponseGroupUserInfo> {
        return groupService.getUserInfo(groupId).getResultFromResponse()
    }

    override suspend fun getFilteredGroupList(query: String, filter: String): Result<ResponseParticipateGroup> {
        return groupService.getFilteredGroupList(query, filter).getResultFromResponse()
    }

    override suspend fun exitGroup(participateId: Int): Result<BaseResponse> {
        return groupService.deleteGroup(participateId).getResultFromResponse()
    }

    override suspend fun editMyGroupInfo(requestEditGroupInfo: RequestEditGroupInfo): Result<BaseResponse> {
        return groupService.editMyGroupInfo(requestEditGroupInfo).getResultFromResponse()
    }

}