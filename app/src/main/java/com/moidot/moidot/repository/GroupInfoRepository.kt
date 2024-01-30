package com.moidot.moidot.repository

import com.moidot.moidot.data.remote.response.ResponseGroupInfo

interface GroupInfoRepository {

    suspend fun getGroupInfo(groupId: Int): Result<ResponseGroupInfo>
}