package com.moidot.moidot.data.remote.datasource.auth

import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn

interface AuthRemoteDataSource {

    suspend fun refreshToken(refreshToken: String): Result<ResponseRefreshToken>

    suspend fun signIn(token: String, platform: String): Result<ResponseSignIn>
}