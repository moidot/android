package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.response.ResponseGroupInfo

interface GroupInfoRepository {

    suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo>
}