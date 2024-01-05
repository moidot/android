package com.moidot.moidot.data.local.datasource.user

interface UserLocalDataSource {
    fun getAccessToken():String?
    fun saveAccessToken(accessToken:String)
    fun getRefreshToken():String?
    fun saveRefreshToken(refreshToken:String)
    fun getOnboardDoneState():Boolean
    fun saveOnboardDoneState(state: Boolean)
}