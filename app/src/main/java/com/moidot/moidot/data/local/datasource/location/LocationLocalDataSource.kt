package com.moidot.moidot.data.local.datasource.location

import com.moidot.moidot.data.local.entity.PlaceEntity

interface LocationLocalDataSource {

    suspend fun insertPlace(place: PlaceEntity)
    suspend fun deletePlace(place:PlaceEntity)

    suspend fun getFavoritePlaces(): List<PlaceEntity>
}