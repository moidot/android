package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.group.GroupPlaceRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.repository.GroupPlaceRepository
import javax.inject.Inject

class GroupPlaceRepositoryImpl @Inject constructor(private val groupPlaceRemoteDataSource: GroupPlaceRemoteDataSource):GroupPlaceRepository {
    override suspend fun bestRegions(groupId: Int): Result<ResponseBestRegion> {
        return groupPlaceRemoteDataSource.getBestRegions(groupId)
    }
}