package com.moidot.moidot.data.local.datasource.user

import android.content.SharedPreferences
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : UserLocalDataSource {
    override fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, "")
    }

    override fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, "")
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    override fun getOnboardDoneState(): Boolean {
        return sharedPreferences.getBoolean(ONBOARD_STATE, false)
    }

    override fun saveOnboardDoneState(state: Boolean) {
        sharedPreferences.edit().putBoolean(ONBOARD_STATE, state).apply()
    }

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val ONBOARD_STATE = "onboard_state"
    }
}