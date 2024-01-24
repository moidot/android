package com.moidot.moidot.presentation.ui.main.group.participate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParticipateGroupViewModel @Inject constructor() : ViewModel() {

    private val _groupName = MutableLiveData<String>("")
    val groupName: LiveData<String> = _groupName

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _transportationTypeTxt = MutableLiveData<String>()
    val transportationTypeTxt: LiveData<String> = _transportationTypeTxt

    private val _locationInfo = MutableLiveData<ResponseSearchPlace.Document>()
    val locationInfo: LiveData<ResponseSearchPlace.Document> = _locationInfo

    // 필드 활성화
    private val _isNickNameFieldActive = MutableLiveData<Boolean>(false)
    val isNickNameFieldActive: LiveData<Boolean> = _isNickNameFieldActive

    // 버튼 활성화 조건
    private val isNickNameInputComplete = MutableLiveData<Boolean>(false) // 닉네임

    private val _isLocationInputComplete = MutableLiveData<Boolean>(false) // 장소
    val isLocationInputComplete: LiveData<Boolean> = _isLocationInputComplete

    private val isTransportationInputComplete = MutableLiveData<Boolean>(false) // 교통 수단

    private val _isParticipateBtnActive = MutableLiveData<Boolean>(false)
    val isParticipateBtnActive: LiveData<Boolean> = _isParticipateBtnActive

    // 에러 메세지
    val nickNameErrorMsg = MutableLiveData<String>()

    fun setLocationInfo(data: ResponseSearchPlace.Document) {
        _locationInfo.value = data
    }

    fun updateInputInfoComplete(infoType: InputInfoType, state: Boolean) { // 입력 상태 갱신
        when (infoType) {
            InputInfoType.NICKNAME_INPUT -> isNickNameInputComplete.value = state
            InputInfoType.LOCATION_INPUT -> _isLocationInputComplete.value = state
            InputInfoType.TRANSPORTATION_INPUT -> isTransportationInputComplete.value = state
        }
        checkLeaderInfoNextBtnActive()
    }

    private fun checkLeaderInfoNextBtnActive() {
        _isParticipateBtnActive.value = isNickNameInputComplete.value!!
                && isLocationInputComplete.value!! && isTransportationInputComplete.value!!
    }

}