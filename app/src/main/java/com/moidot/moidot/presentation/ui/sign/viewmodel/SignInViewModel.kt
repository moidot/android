package com.moidot.moidot.presentation.ui.sign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseSignIn
import com.moidot.moidot.domain.repository.AuthRepository
import com.moidot.moidot.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signInResult = MutableLiveData<ResponseSignIn.Data>()
    val signInResult: LiveData<ResponseSignIn.Data> = _signInResult

    fun signInWithSocialToken(token: String, platform: String) {
        viewModelScope.launch { // TODO refresh 토큰 및 실패 처리
            authRepository.signIn(token, platform).onSuccess {
                _signInResult.value = it.data
                saveUserTokens(it.data.accessToken, it.data.refreshToken)
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