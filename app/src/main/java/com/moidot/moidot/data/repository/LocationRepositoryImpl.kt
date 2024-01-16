package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.location.LocationRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationRemoteDataSource: LocationRemoteDataSource) :
    LocationRepository {

    override suspend fun searchPlace(
        query: String,
    ): Result<ResponseSearchPlace> {
        return locationRemoteDataSource.searchPlace(query = query)
    }
}