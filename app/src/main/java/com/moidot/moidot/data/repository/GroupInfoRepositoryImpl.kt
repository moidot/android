package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupInfoRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.domain.repository.GroupInfoRepository
import javax.inject.Inject

class GroupInfoRepositoryImpl @Inject constructor(private val groupInfoRemoteDataSource: GroupInfoRemoteDataSource) : GroupInfoRepository {
    override suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo> {
        return groupInfoRemoteDataSource.getGroupInfo(groupId)
    }
}