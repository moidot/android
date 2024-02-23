package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.auth.AuthRemoteDataSource
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn
import com.moidot.moidot.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource) : AuthRepository {
    override suspend fun refreshToken(refreshToken: String): Result<ResponseRefreshToken> {
        return authRemoteDataSource.refreshToken(refreshToken)
    }

    override suspend fun signIn(token: String, platform: String): Result<ResponseSignIn> {
        return authRemoteDataSource.signIn(token, platform)
    }

    override suspend fun logout(): Result<BaseResponse> {
        return authRemoteDataSource.logout()
    }
}