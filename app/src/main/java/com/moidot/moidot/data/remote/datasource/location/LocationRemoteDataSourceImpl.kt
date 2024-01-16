package com.moidot.moidot.data.remote.datasource.location

import com.moidot.moidot.data.api.LocationService
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(private val locationService: LocationService) : LocationRemoteDataSource {
    override suspend fun searchPlace(query: String): Result<ResponseSearchPlace> {
        val response = locationService.searchPlace(query = query)
        if (response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalStateException(response.message()))
    }
}