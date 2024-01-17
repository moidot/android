package com.moidot.moidot.data.local.datasource.location

import com.moidot.moidot.data.local.dao.PlaceDao
import com.moidot.moidot.data.local.entity.PlaceEntity
import javax.inject.Inject

class LocationLocalDataSourceImpl @Inject constructor(
    private val placeDao: PlaceDao
) : LocationLocalDataSource {
    override suspend fun insertPlace(place: PlaceEntity) {
        placeDao.insertPlace(place)
    }

    override suspend fun deletePlace(place: PlaceEntity) {
        placeDao.deletePlace(place)
    }

    override suspend fun getFavoritePlaces(): List<PlaceEntity> {
        return placeDao.getFavoritePlaces()
    }

}