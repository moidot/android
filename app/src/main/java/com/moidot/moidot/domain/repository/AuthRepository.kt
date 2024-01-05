package com.moidot.moidot.domain.repository

import com.moidot.moidot.data.remote.response.ResponseSignIn

interface AuthRepository {
    suspend fun signIn(token: String, platform:String): Result<ResponseSignIn>
}