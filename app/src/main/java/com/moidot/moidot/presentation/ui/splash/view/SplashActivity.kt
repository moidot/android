package com.moidot.moidot.presentation.ui.splash.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.presentation.ui.main.MainActivity
import com.moidot.moidot.presentation.ui.onboard.view.OnboardActivity
import com.moidot.moidot.presentation.ui.sign.view.SignInActivity
import com.moidot.moidot.presentation.ui.splash.model.Screen
import com.moidot.moidot.presentation.ui.splash.model.Screen.ONBOARD
import com.moidot.moidot.presentation.ui.splash.model.Screen.SIGN_IN
import com.moidot.moidot.presentation.ui.splash.model.Screen.HOME
import com.moidot.moidot.presentation.ui.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.screenType.observe(this) {
            setNextScreen(it)
        }
    }

    private fun setNextScreen(screen: Screen) {
        val intent = when (screen) {
            ONBOARD -> Intent(this, OnboardActivity::class.java)
            SIGN_IN -> Intent(this, SignInActivity::class.java)
            HOME -> Intent(this, MainActivity::class.java)
        }
        moveToNext(intent)
    }

    private fun moveToNext(intent: Intent) {
        viewModel.viewModelScope.launch {
            delay(1000L)
            startActivity(intent)
            finish()
        }
    }
}