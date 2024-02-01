package com.moidot.moidot.presentation.onboard.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.moidot.moidot.R
import com.moidot.moidot.data.local.datasource.user.UserLocalDataSourceImpl.Companion.ACCESS_TOKEN
import com.moidot.moidot.databinding.ActivityOnboardBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.MainActivity
import com.moidot.moidot.presentation.onboard.viewmodel.OnboardViewModel
import com.moidot.moidot.presentation.sign.view.SignInActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardActivity : BaseActivity<ActivityOnboardBinding>(R.layout.activity_onboard) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: OnboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.onboard_fcv) as NavHostFragment
        navHost.findNavController().setGraph(R.navigation.onboard_nav_graph, intent.extras)
    }

    fun exitOnboard() {
        finish()
        viewModel.saveOnboardDone()
        determineNextScreen()
    }

    /** 이미 로그인한 정보가 존재할 경우 MainActivity 로 이동
     * scheme을 통해 로그인 작업을 완료한 경우를 대비해 추가한 함수입니다.
     * */
    private fun determineNextScreen() {
        if (sharedPreferences.getString(ACCESS_TOKEN, null) == null) {
            startActivity(Intent(this, SignInActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}