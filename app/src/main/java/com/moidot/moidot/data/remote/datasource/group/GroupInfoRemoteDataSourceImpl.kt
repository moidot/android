package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupInfoService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import javax.inject.Inject

class GroupInfoRemoteDataSourceImpl @Inject constructor(private val groupInfoService: GroupInfoService) : GroupInfoRemoteDataSource {
    override suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo> {
        return groupInfoService.getGroupInfo(groupId).getResultFromResponse()
    }
}