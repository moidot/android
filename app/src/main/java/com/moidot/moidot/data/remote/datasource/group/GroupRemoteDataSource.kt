package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.response.ResponseParticipateGroup

interface GroupRemoteDataSource {

    suspend fun getMyGroupList(): Result<ResponseParticipateGroup>
}