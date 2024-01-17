package com.moidot.moidot.data.repository

import com.moidot.moidot.data.local.datasource.location.LocationLocalDataSource
import com.moidot.moidot.data.local.entity.PlaceEntity
import com.moidot.moidot.data.remote.datasource.location.LocationRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationLocalDataSource: LocationLocalDataSource,
    private val locationRemoteDataSource: LocationRemoteDataSource
) :
    LocationRepository {

    override suspend fun searchPlace(
        query: String,
    ): Result<ResponseSearchPlace> {
        return locationRemoteDataSource.searchPlace(query = query)
    }

    override suspend fun insertPlace(place: PlaceEntity) {
        locationLocalDataSource.insertPlace(place)
    }

    override suspend fun deletePlace(place: PlaceEntity) {
        locationLocalDataSource.deletePlace(place)
    }

    override suspend fun getFavoritePlaces(): List<PlaceEntity> {
        return locationLocalDataSource.getFavoritePlaces()
    }
}