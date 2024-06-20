package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupPlaceService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.data.remote.response.ResponseRecommendPlace
import javax.inject.Inject

class GroupPlaceRemoteDataSourceImpl @Inject constructor(private val groupPlaceService: GroupPlaceService) : GroupPlaceRemoteDataSource {
    override suspend fun getBestRegions(groupId: Int): Result<ResponseBestRegion> {
        return groupPlaceService.getBestRegions(groupId).getResultFromResponse()
    }

    override suspend fun getRecommendPlace(x: Double, y: Double, local: String, keyword: String): Result<ResponseRecommendPlace> {
        return groupPlaceService.getRecommendPlace(x, y, local, keyword).getResultFromResponse()
    }
}