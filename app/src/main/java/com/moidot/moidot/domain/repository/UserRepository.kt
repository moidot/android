package com.moidot.moidot.domain.repository

interface UserRepository {
    fun getAccessToken():String?
    fun saveAccessToken(accessToken:String)
    fun getRefreshToken():String?
    fun saveRefreshToken(refreshToken:String)
    fun getOnboardDoneState():Boolean
    fun saveOnboardDoneState(state: Boolean)
}