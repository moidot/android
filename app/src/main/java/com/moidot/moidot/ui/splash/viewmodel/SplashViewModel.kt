package com.moidot.moidot.ui.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.repository.UserRepository
import com.moidot.moidot.ui.splash.model.Screen
import com.moidot.moidot.ui.splash.model.Screen.ONBOARD
import com.moidot.moidot.ui.splash.model.Screen.SIGN_IN
import com.moidot.moidot.ui.splash.model.Screen.HOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _screenType = MutableLiveData<Screen>()
    val screenType: LiveData<Screen> = _screenType

    init {
        checkFirstOrLogin()
    }

    private fun checkFirstOrLogin() {
        _screenType.value = when {
            !userRepository.getOnboardDoneState() -> ONBOARD
            userRepository.getAccessToken().isNullOrEmpty() -> SIGN_IN
            else -> HOME
        }
    }
}