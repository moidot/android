package com.moidot.moidot.presentation.ui.onboard.view

import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityOnboardBinding
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

}