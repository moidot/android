package com.moidot.moidot.repository

import com.moidot.moidot.data.remote.response.ResponseBestRegion

interface GroupPlaceRepository {
    suspend fun bestRegions(groupId: Int): Result<ResponseBestRegion>
}