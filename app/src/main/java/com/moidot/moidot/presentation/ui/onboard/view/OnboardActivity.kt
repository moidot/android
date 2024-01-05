package com.moidot.moidot.presentation.ui.onboard.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityOnboardBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.ui.onboard.viewmodel.OnboardViewModel
import com.moidot.moidot.presentation.ui.sign.view.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardActivity : BaseActivity<ActivityOnboardBinding>(R.layout.activity_onboard) {

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
        startActivity(Intent(this, SignInActivity::class.java))
    }
}