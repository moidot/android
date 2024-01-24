package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseValidateNickname

interface GroupRepository {

    suspend fun getMyGroupList(): Result<ResponseParticipateGroup>

    suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup>

    suspend fun validateNickname(groupId: Int, nickname: String): Result<ResponseValidateNickname>
}