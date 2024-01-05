package com.moidot.moidot.presentation.ui

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.moidot.moidot.BuildConfig.KAKAO_NATIVE_APP_KEY
import com.moidot.moidot.BuildConfig.NAVER_CLIENT_ID
import com.moidot.moidot.BuildConfig.NAVER_CLIENT_SECRET_KEY
import com.moidot.moidot.R
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoidotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSdk()
    }

    private fun initSdk() {
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
        NaverIdLoginSDK.initialize(this, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET_KEY, getString(R.string.app_name))
    }

    companion object {
        const val MOIDOT_APP = "moidot_app"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }
}