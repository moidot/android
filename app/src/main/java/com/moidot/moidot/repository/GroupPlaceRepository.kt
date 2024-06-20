package com.moidot.moidot.repository

import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.data.remote.response.ResponseRecommendPlace

interface GroupPlaceRepository {
    suspend fun bestRegions(groupId: Int): Result<ResponseBestRegion>

    suspend fun recommendPlace(x: Double, y: Double, local: String, keyword: String): Result<ResponseRecommendPlace>
}