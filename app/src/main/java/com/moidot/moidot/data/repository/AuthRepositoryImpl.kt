package com.moidot.moidot.data.repository

import com.moidot.moidot.data.remote.datasource.auth.AuthRemoteDataSource
import com.moidot.moidot.data.remote.response.ResponseSignIn
import com.moidot.moidot.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource):AuthRepository {

    override suspend fun signIn(token: String, platform:String): Result<ResponseSignIn> {
        return authRemoteDataSource.signIn(token, platform)
    }
}