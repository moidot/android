package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.response.ResponseParticipateGroup

interface GroupRepository {

    suspend fun getMyGroupList(): Result<ResponseParticipateGroup>
}