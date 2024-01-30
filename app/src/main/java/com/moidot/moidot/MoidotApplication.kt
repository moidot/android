package com.moidot.moidot

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.moidot.moidot.BuildConfig.KAKAO_NATIVE_APP_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoidotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSdk()
    }

    private fun initSdk() {
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
    }

    companion object {
        const val MOIDOT_APP = "moidot_app"
    }
}