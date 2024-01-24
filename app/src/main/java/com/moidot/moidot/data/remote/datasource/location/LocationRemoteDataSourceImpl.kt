package com.moidot.moidot.data.remote.datasource.location

import com.moidot.moidot.data.api.LocationService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.ResponseCoorToAddress
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(private val locationService: LocationService) : LocationRemoteDataSource {
    override suspend fun searchPlace(query: String): Result<ResponseSearchPlace> {
        return locationService.getSearchPlace(query = query).getResultFromResponse()
    }

    override suspend fun getAddressFromCoord(longitude: Double, latitude: Double): Result<ResponseCoorToAddress> {
        return locationService.getCoordToAddress(longitude = longitude, latitude = latitude).getResultFromResponse()
    }
}