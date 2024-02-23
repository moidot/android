package com.moidot.moidot.data.repository

import com.moidot.moidot.data.data.UserInfo
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSource
import com.moidot.moidot.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userLocalDataSource: UserLocalDataSource) : UserRepository {
    override fun removeAllToken() {
       userLocalDataSource.removeAllToken()
    }

    override fun getAccessToken(): String? {
        return userLocalDataSource.getAccessToken()
    }

    override fun saveAccessToken(accessToken: String) {
        userLocalDataSource.saveAccessToken(accessToken)
    }

    override fun getUserInfo(): UserInfo {
        return userLocalDataSource.getUserInfo()
    }

    override fun saveUserInfo(userInfo: UserInfo) {
        return userLocalDataSource.saveUserInfo(userInfo)
    }

    override fun getRefreshToken(): String? {
        return userLocalDataSource.getRefreshToken()
    }

    override fun saveRefreshToken(refreshToken: String) {
        userLocalDataSource.saveRefreshToken(refreshToken)
    }

    override fun getOnboardDoneState(): Boolean {
        return userLocalDataSource.getOnboardDoneState()
    }

    override fun saveOnboardDoneState(state: Boolean) {
        userLocalDataSource.saveOnboardDoneState(state)
    }
}