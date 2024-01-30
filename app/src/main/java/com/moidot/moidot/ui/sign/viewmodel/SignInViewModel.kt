package com.moidot.moidot.ui.sign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.repository.AuthRepository
import com.moidot.moidot.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginSuccessState = MutableLiveData<Boolean>()
    val loginSuccessState: LiveData<Boolean> = _loginSuccessState

    fun signInWithSocialToken(token: String, platform: String) {
        viewModelScope.launch {
            authRepository.signIn(token, platform).onSuccess {
                saveUserTokens(it.data.accessToken, it.data.refreshToken)
                _loginSuccessState.value = true
            }
        }
    }

    private fun saveUserTokens(accessToken: String, refreshToken: String) {
        userRepository.apply {
            saveAccessToken(accessToken)
            saveRefreshToken(refreshToken)
        }
    }
}