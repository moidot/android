package com.moidot.moidot.data.remote

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.moidot.moidot.BuildConfig
import com.moidot.moidot.data.api.AuthService
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl.Companion.ACCESS_TOKEN
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl.Companion.ONBOARD_STATE
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl.Companion.REFRESH_TOKEN
import com.moidot.moidot.presentation.sign.view.SignInActivity
import com.moidot.moidot.util.Constant.REFRESH_DONE_STATE
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val newAccessToken: String? = async { getNewAccessToken() }.await()
        if (newAccessToken == null) moveToSignIn()
        return@runBlocking refreshToken(newAccessToken, response)
    }

    private suspend fun getNewAccessToken(): String? {
        val refreshToken: String = sharedPreferences.getString(REFRESH_TOKEN, null) ?: return null
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        val service = retrofit.create(AuthService::class.java)
        return service.getRefreshToken(refreshToken).body()?.data?.accessToken ?: run { null }
    }

    private fun moveToSignIn() {
        sharedPreferences.edit().clear().apply()
        sharedPreferences.edit().putBoolean(ONBOARD_STATE, true).apply() // 온보딩 읽음 처리 유지
        Intent(context, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.putExtra(REFRESH_DONE_STATE, true)
            context.startActivity(this)
        }
    }

    private fun refreshToken(newAccessToken: String?, response: Response): Request? {
        if (newAccessToken != null) {
            sharedPreferences.edit().putString(ACCESS_TOKEN, newAccessToken).apply()
            return response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", newAccessToken)
                .build()
        }
        return null
    }

}