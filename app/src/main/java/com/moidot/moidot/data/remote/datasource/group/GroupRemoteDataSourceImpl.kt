package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupService
import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import javax.inject.Inject

class GroupRemoteDataSourceImpl @Inject constructor(private val groupService: GroupService):GroupRemoteDataSource {
    override suspend fun getMyGroupList(): Result<ResponseParticipateGroup> {
        val response = groupService.getParticipateGroup()
        if (response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalStateException(response.message()))
    }

    override suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup> {
        val response = groupService.createGroup(requestCreateGroup)
        if (response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalStateException(response.message()))
    }
}