package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSource
import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(private val groupRemoteDataSource: GroupRemoteDataSource) : GroupRepository {
    override suspend fun getMyGroupList(): Result<ResponseParticipateGroup> {
        return groupRemoteDataSource.getMyGroupList()
    }

    override suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup> {
        return groupRemoteDataSource.createGroup(requestCreateGroup)
    }

}