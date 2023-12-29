package com.moidot.moidot.data.remote.datasource.auth

import com.moidot.moidot.data.remote.request.RequestSignIn
import com.moidot.moidot.data.remote.response.ResponseSignIn

interface AuthRemoteDataSource {

    suspend fun signIn(body: RequestSignIn): Result<ResponseSignIn>
}