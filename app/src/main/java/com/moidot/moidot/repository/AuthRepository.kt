package com.moidot.moidot.repository

import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn

interface AuthRepository {

    suspend fun refreshToken(refreshToken: String): Result<ResponseRefreshToken>
    suspend fun signIn(token: String, platform: String): Result<ResponseSignIn>

    suspend fun logout(): Result<BaseResponse>

    suspend fun withdrawal(): Result<BaseResponse>
}