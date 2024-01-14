package com.moidot.moidot.data.remote

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl.Companion.REFRESH_TOKEN
import com.moidot.moidot.domain.repository.AuthRepository
import com.moidot.moidot.presentation.ui.sign.view.SignInActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val authRepository: dagger.Lazy<AuthRepository>,
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val newAccessToken: String? = async { getNewAccessToken() }.await()
        return@runBlocking refreshToken(newAccessToken, response)
    }

    private suspend fun getNewAccessToken(): String? {
        val refreshToken: String = sharedPreferences.getString(REFRESH_TOKEN, null) ?: return null
        try {
            val response = authRepository.get().refreshToken(refreshToken)
            return response.getOrThrow().data.accessToken
        } catch (e: Exception) {
            moveToLogin()
        }
        return null
    }

    private fun moveToLogin() {
        Intent(context, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }
    }

    private fun refreshToken(newAccessToken: String?, response: Response): Request? {
        if (newAccessToken != null) {
            return response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", newAccessToken)
                .build()
        }
        return null
    }

}