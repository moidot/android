package com.moidot.moidot.presentation.main.mypage.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.BuildConfig
import com.moidot.moidot.BuildConfig.VERSION_NAME
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.LOGOUT_FAIL
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.LOGOUT_SUCCESS
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.WITHDRAWAL_FAIL
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.WITHDRAWAL_SUCCESS
import com.moidot.moidot.repository.AuthRepository
import com.moidot.moidot.repository.UserRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val versionName = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val platform = MutableLiveData<String>()

    private val _userState = MutableSingleLiveData<UserState>()
    val userState: SingleLiveData<UserState> = _userState

    init {
        loadAppInfo()
        loadUserInfo()
    }

    private fun loadAppInfo() {
        versionName.value = "버전 $VERSION_NAME"
    }

    private fun loadUserInfo() {
        userRepository.getUserInfo().apply {
            userName.value = "${name}님"
            userEmail.value = email
            platform.value = type
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().onSuccess {
                if (it.code == 0) {
                    removeUserInfoPrefs()
                    _userState.setValue(LOGOUT_SUCCESS)
                } else {
                    _userState.setValue(LOGOUT_FAIL)
                }
            }.onFailure {
                _userState.setValue(LOGOUT_FAIL)
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            authRepository.withdrawal().onSuccess {
                if (it.code == 0) {
                    removeUserInfoPrefs()
                    _userState.setValue(WITHDRAWAL_SUCCESS)
                } else {
                    _userState.setValue(WITHDRAWAL_FAIL)
                }
            }.onFailure {
                _userState.setValue(WITHDRAWAL_FAIL)
            }
        }
    }

    private fun removeUserInfoPrefs() {
        userRepository.removeAllToken()
        userRepository.saveOnboardDoneState(true) // 온보딩은 '읽음' 상태로 남겨둔다.
    }
}