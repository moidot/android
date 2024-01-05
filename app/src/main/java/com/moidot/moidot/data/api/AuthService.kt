package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.ResponseSignIn
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {

    @GET("/auth/signin/token")
    suspend fun getSignIn(@Query("token") token: String, @Query("platform") platform: String): Response<ResponseSignIn>
}