package com.moidot.moidot.data.remote

import android.content.SharedPreferences
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl.Companion.ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(private val sharedPreferences: SharedPreferences) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val accessToken: String? = sharedPreferences.getString(ACCESS_TOKEN, null)
        if (accessToken != null) {
            builder.addHeader("Authorization", "Bearer $accessToken")
        }
        return chain.proceed(builder.build())
    }
}