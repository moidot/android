package com.moidot.moidot.presentation.main.mypage.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.LOGOUT_FAIL
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.LOGOUT_SUCCESS
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

    private val _userState = MutableSingleLiveData<UserState>()
    val userState: SingleLiveData<UserState> = _userState

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

    private fun removeUserInfoPrefs() {
        userRepository.removeAllToken()
        userRepository.saveOnboardDoneState(true) // 온보딩은 '읽음' 상태로 남겨둔다.
    }

    // TODO 회원 탈퇴
}