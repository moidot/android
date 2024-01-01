package com.moidot.moidot.presentation.ui.onboard.view

import android.content.Intent
import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityOnboardBinding
import com.moidot.moidot.presentation.ui.auth.signin.view.SignInActivity
import com.moidot.moidot.presentation.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardActivity : BaseActivity<ActivityOnboardBinding>(R.layout.activity_onboard) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        initFragmentView()
    }

    private fun initFragmentView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.onboard_fcv, OnboardFirstFragment())
            .commit()
    }

    fun exitOnboard() {
        finish()
        // TODO 온보딩 읽음 여부 prefs 저장
        startActivity(Intent(this, SignInActivity::class.java))
    }
}