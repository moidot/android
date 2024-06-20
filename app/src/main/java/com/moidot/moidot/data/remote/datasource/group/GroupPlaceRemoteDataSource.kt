package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.data.remote.response.ResponseRecommendPlace

interface GroupPlaceRemoteDataSource {

    suspend fun getBestRegions(groupId: Int): Result<ResponseBestRegion>

    suspend fun getRecommendPlace(x: Double, y: Double, local: String, keyword: String): Result<ResponseRecommendPlace>
}