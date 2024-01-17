package com.moidot.moidot.data.remote.datasource.location

import com.moidot.moidot.data.remote.response.ResponseSearchPlace

interface LocationRemoteDataSource {
    suspend fun searchPlace(query: String): Result<ResponseSearchPlace>
}