package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.request.RequestSignIn
import com.moidot.moidot.data.remote.response.ResponseSignIn
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthService {

    @GET("/auth/signin")
    suspend fun getSignIn(@Body body: RequestSignIn): Response<ResponseSignIn>
}