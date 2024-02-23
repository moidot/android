package com.moidot.moidot.data.remote.datasource.auth

import com.moidot.moidot.data.api.AuthService
import com.moidot.moidot.data.remote.datasource.getResultFromResponse
import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val authService: AuthService) : AuthRemoteDataSource {
    override suspend fun refreshToken(refreshToken: String): Result<ResponseRefreshToken> {
        return authService.getRefreshToken(refreshToken).getResultFromResponse()
    }

    override suspend fun signIn(token: String, platform: String): Result<ResponseSignIn> {
        return authService.getSignIn(token, platform).getResultFromResponse()
    }

    override suspend fun logout(): Result<BaseResponse> {
        return authService.deleteUserToken().getResultFromResponse()
    }
}