package com.moidot.moidot.repository

import com.kakao.sdk.user.model.User
import com.moidot.moidot.data.data.UserInfo

interface UserRepository {
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