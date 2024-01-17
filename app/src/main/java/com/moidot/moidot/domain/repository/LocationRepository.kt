package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.local.entity.PlaceEntity
import com.moidot.moidot.data.remote.response.ResponseSearchPlace

interface LocationRepository {

    suspend fun searchPlace(query: String): Result<ResponseSearchPlace>

    suspend fun insertPlace(place: PlaceEntity)
    suspend fun deletePlace(place: PlaceEntity)

    suspend fun getFavoritePlaces(): List<PlaceEntity>
}
