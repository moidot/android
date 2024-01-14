package com.moidot.moidot.data.remote.datasource.auth

import com.moidot.moidot.data.api.AuthService
import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val authService: AuthService) : AuthRemoteDataSource {
    override suspend fun refreshToken(refreshToken:String): Result<ResponseRefreshToken> {
        val response = authService.getRefreshToken(refreshToken)
        if (response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalStateException(response.message()))
    }

    override suspend fun signIn(token: String, platform: String): Result<ResponseSignIn> {
        val response = authService.getSignIn(token, platform)
        if (response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalStateException(response.message()))
    }
}