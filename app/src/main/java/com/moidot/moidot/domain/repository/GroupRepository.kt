package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.request.RequestPostParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseCheckNicknameDuplication
import com.moidot.moidot.data.remote.response.ResponsePostParticipateGroup

interface GroupRepository {

    suspend fun getMyGroupList(): Result<ResponseParticipateGroup>

    suspend fun createGroup(requestCreateGroup: RequestCreateGroup): Result<ResponseCreateGroup>

    suspend fun participateGroup(requestPostParticipateGroup: RequestPostParticipateGroup): Result<ResponsePostParticipateGroup>

    suspend fun checkNicknameDuplication(groupId: Int, nickname: String): Result<ResponseCheckNicknameDuplication>
}