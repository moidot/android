package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSource
import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.request.RequestPostParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseCheckNicknameDuplication
import com.moidot.moidot.data.remote.response.ResponseGroupUserInfo
import com.moidot.moidot.data.remote.response.ResponsePostParticipateGroup
import com.moidot.moidot.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(private val groupRemoteDataSource: GroupRemoteDataSource) : GroupRepository {
    override suspend fun getMyGroupList(): Result<ResponseParticipateGroup> {
        return groupRemoteDataSource.getMyGroupList()
    }

    override suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup> {
        return groupRemoteDataSource.createGroup(requestCreateGroup)
    }

    override suspend fun participateGroup(requestPostParticipateGroup: RequestPostParticipateGroup): Result<ResponsePostParticipateGroup> {
        return groupRemoteDataSource.participateGroup(requestPostParticipateGroup)
    }

    override suspend fun checkNicknameDuplication(groupId: Int, nickname: String): Result<ResponseCheckNicknameDuplication> {
        return groupRemoteDataSource.checkNicknameDuplication(groupId, nickname)
    }

    override suspend fun getUserInfo(groupId: Int): Result<ResponseGroupUserInfo> {
        return groupRemoteDataSource.getUserInfo(groupId)
    }

}