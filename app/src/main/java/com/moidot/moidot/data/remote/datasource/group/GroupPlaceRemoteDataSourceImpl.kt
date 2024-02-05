package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.api.GroupPlaceService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import javax.inject.Inject

class GroupPlaceRemoteDataSourceImpl @Inject constructor(private val groupPlaceService: GroupPlaceService) : GroupPlaceRemoteDataSource {
    override suspend fun getBestRegions(groupId: Int): Result<ResponseBestRegion> {
        return groupPlaceService.getBestRegions(groupId).getResultFromResponse()
    }
}