package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.request.RequestSignIn
import com.moidot.moidot.data.remote.response.ResponseSignIn

interface AuthRepository {

    suspend fun signIn(body: RequestSignIn): Result<ResponseSignIn>
}