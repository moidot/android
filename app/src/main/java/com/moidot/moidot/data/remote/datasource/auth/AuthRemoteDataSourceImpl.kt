package com.moidot.moidot.data.remote.datasource.auth

import com.moidot.moidot.data.api.AuthService
import com.moidot.moidot.data.remote.request.RequestSignIn
import com.moidot.moidot.data.remote.response.ResponseSignIn
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val authService: AuthService) : AuthRemoteDataSource {
    override suspend fun signIn(body: RequestSignIn): Result<ResponseSignIn> {
        val response = authService.getSignIn(body)
        if (response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalStateException(response.message()))
    }
}