package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn
import retrofit2.http.Header

interface AuthRepository {

    suspend fun refreshToken(refreshToken: String): Result<ResponseRefreshToken>
    suspend fun signIn(token: String, platform: String): Result<ResponseSignIn>
}