package com.moidot.moidot.data.remote.datasource.location

import com.moidot.moidot.data.remote.response.ResponseCoorToAddress
import com.moidot.moidot.data.remote.response.ResponseSearchPlace

interface LocationRemoteDataSource {
    suspend fun searchPlace(query: String): Result<ResponseSearchPlace>

    suspend fun getAddressFromCoord(longitude: Double, latitude: Double): Result<ResponseCoorToAddress>
}