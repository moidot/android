package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.response.ResponseSearchPlace

interface LocationRepository {

    suspend fun searchPlace(query: String): Result<ResponseSearchPlace>
}
