package com.moidot.moidot.data.local.datasource.user

import com.moidot.moidot.data.data.UserInfo

interface UserLocalDataSource {
    fun removeAllToken()
    fun getAccessToken(): String?
    fun saveAccessToken(accessToken: String)
    fun getUserInfo(): UserInfo
    fun saveUserInfo(userInfo: UserInfo)
    fun getRefreshToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun getOnboardDoneState(): Boolean
    fun saveOnboardDoneState(state: Boolean)
}