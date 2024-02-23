package com.moidot.moidot.data.local.datasource.user

import android.content.SharedPreferences
import com.moidot.moidot.data.data.UserInfo
import com.moidot.moidot.util.Constant.USER_INFO_EMAIL
import com.moidot.moidot.util.Constant.USER_INFO_ID
import com.moidot.moidot.util.Constant.USER_INFO_NAME
import com.moidot.moidot.util.Constant.USER_INFO_TYPE
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : UserLocalDataSource {
    override fun removeAllToken() {
        sharedPreferences.edit().clear().apply()
    }

    override fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, "")
    }

    override fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    override fun getUserInfo(): UserInfo {
        return UserInfo(
            userId = sharedPreferences.getInt(USER_INFO_ID, 0),
            type = sharedPreferences.getString(USER_INFO_TYPE, "") ?: "",
            name = sharedPreferences.getString(USER_INFO_NAME, "") ?: "",
            email = sharedPreferences.getString(USER_INFO_EMAIL, "") ?: "",
        )
    }

    override fun saveUserInfo(userInfo: UserInfo) {
        sharedPreferences.edit().apply {
            putInt(USER_INFO_ID, userInfo.userId)
            putString(USER_INFO_TYPE, userInfo.type)
            putString(USER_INFO_NAME, userInfo.name)
            putString(USER_INFO_EMAIL, userInfo.email)
        }.apply()
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