package com.moidot.moidot.presentation.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {

    init {
        checkFirstOrLogin()
    }

    // TODO Repoitory를 통해 access token 정상 여부 검사해야함
    private fun checkFirstOrLogin() {
        viewModelScope.launch {
            delay(1000)
        }
    }
}