package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(private val groupRemoteDataSource: GroupRemoteDataSource) : GroupRepository {
    override suspend fun getMyGroupList(): Result<ResponseParticipateGroup> {
        return groupRemoteDataSource.getMyGroupList()
    }

}