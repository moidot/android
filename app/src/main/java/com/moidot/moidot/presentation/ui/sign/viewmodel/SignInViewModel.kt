package com.moidot.moidot.presentation.ui.sign.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseSignIn
import com.moidot.moidot.domain.repository.AuthRepository
import com.moidot.moidot.presentation.ui.sign.model.Platform.KAKAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _signInResult = MutableLiveData<ResponseSignIn.Data>()
    val signInResult : LiveData<ResponseSignIn.Data> = _signInResult

    fun signInWithKakao(kakaoAtk: String) {
        viewModelScope.launch {
            authRepository.signIn(token = kakaoAtk, platform = KAKAO.name).onSuccess {
                _signInResult.value = it.data
            }
        }
    }
}