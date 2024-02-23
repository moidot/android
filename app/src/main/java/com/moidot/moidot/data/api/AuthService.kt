package com.moidot.moidot.data.api

import com.moidot.moidot.data.remote.response.BaseResponse
import com.moidot.moidot.data.remote.response.ResponseRefreshToken
import com.moidot.moidot.data.remote.response.ResponseSignIn
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET("/auth/signin/token")
    suspend fun getSignIn(@Query("token") token: String, @Query("platform") platform: String): Response<ResponseSignIn>

    @GET("/auth/refresh")
    suspend fun getRefreshToken(@Header("refreshToken") refreshToken: String): Response<ResponseRefreshToken>

    @DELETE("/auth/logout")
    suspend fun deleteUserToken(): Response<BaseResponse>
}