package com.moidot.moidot.presentation.ui.main.group.create.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType.NICKNAME_INPUT
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType.LOCATION_INPUT
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType.TRANSPORTATION_INPUT
import javax.inject.Inject

class CreateGroupViewModel @Inject constructor() : ViewModel() {

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> = _nickName

    private val _transportationTypeTxt = MutableLiveData<String>()
    val transportationTypeTxt: LiveData<String> = _transportationTypeTxt

    private val _locationInfo = MutableLiveData<ResponseSearchPlace.Document>()
    val locationInfo: LiveData<ResponseSearchPlace.Document> = _locationInfo

    // 필드 활성화
    private val _isGroupNameFieldActive = MutableLiveData<Boolean>(false)
    val isGroupNameFieldActive: LiveData<Boolean> = _isGroupNameFieldActive

    private val _isNickNameFieldActive = MutableLiveData<Boolean>(false)
    val isNickNameFieldActive: LiveData<Boolean> = _isNickNameFieldActive

    // 버튼 활성화 조건
    private val _isNickNameInputComplete = MutableLiveData<Boolean>(false) // 닉네임
    val isNickNameInputComplete: LiveData<Boolean> = _isNickNameInputComplete

    private val _isLocationInputComplete = MutableLiveData<Boolean>(false) // 장소
    val isLocationInputComplete: LiveData<Boolean> = _isLocationInputComplete

    private val _isTransportationInputComplete = MutableLiveData<Boolean>(false) // 교통 수단
    val isTransportationInputComplete: LiveData<Boolean> = _isTransportationInputComplete

    // 버튼 활성화
    private val _isGroupInfoNextBtnActive = MutableLiveData<Boolean>(false)
    val isGroupInfoNextBtnActive: LiveData<Boolean> = _isGroupInfoNextBtnActive

    private val _isLeaderInfoNextBtnActive = MutableLiveData<Boolean>(false)
    val isLeaderInfoNextBtnActive: LiveData<Boolean> = _isLeaderInfoNextBtnActive

    // 에러 메세지
    val groupNameErrorMsg = MutableLiveData<String>()
    val nickNameErrorMsg = MutableLiveData<String>()

    // 이전 화면 작업 완료 여부 체크
    private val isUserInputAlreadyDone = MutableLiveData<Boolean>(false)

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setNickName(name: String) {
        _nickName.value = name
    }

    fun setTransportationTypeTxt(transportationType: String) {
        _transportationTypeTxt.value = transportationType
    }

    fun setGroupNameFieldActive(value: Any) {
        setFieldActive(_isGroupNameFieldActive, value)
    }

    fun setNickNameFieldActive(value: Any) {
        setFieldActive(_isNickNameFieldActive, value)
    }

    fun setGroupInfoNextBtnActive(value: Any) {
        setFieldActive(_isGroupInfoNextBtnActive, value)
    }

    private fun setFieldActive(field: MutableLiveData<Boolean>, value: Any) {
        field.value = when (value) {
            is String -> value.isNotEmpty()
            is Boolean -> value
            else -> throw IllegalArgumentException("Value Error!")
        }
    }

    fun checkIsValidGroupName(): Boolean {
        val groupName = groupName.value!!
        val specialChars = "!#$%&'()*+,/:;=?@[]_~-|{} " // 사용 가능한 특수문자
        val disallowedChars = groupName.filter { !it.isLetterOrDigit() && it !in specialChars }

        val errorMsg = when {
            disallowedChars.isNotEmpty() -> "'${disallowedChars}'는(은) 사용할 수 없습니다."
            groupName.all { !it.isLetterOrDigit() } -> "특수 문자만으로는 모임명을 만들 수 없습니다."
            else -> ""
        }

        return updateGroupInfoStates(errorMsg)
    }

    private fun updateGroupInfoStates(errorMsg: String): Boolean {
        groupNameErrorMsg.value = errorMsg

        val isValid = errorMsg.isEmpty()
        _isGroupInfoNextBtnActive.value = isValid

        return isValid
    }

    fun updateUserInputAlreadyDoneState() {
        isUserInputAlreadyDone.value = true
    }

    fun checkUserInputDoneState() {
        if (isUserInputAlreadyDone.value!!) {
            _isGroupNameFieldActive.value = false
        }
    }

    fun setLocationInfo(data: ResponseSearchPlace.Document) {
        _locationInfo.value = data
    }

    fun updateInputInfoComplete(infoType: InputInfoType, state: Boolean) { // 입력 상태 갱신
        when (infoType) {
            NICKNAME_INPUT -> _isNickNameInputComplete.value = state
            LOCATION_INPUT -> _isLocationInputComplete.value = state
            TRANSPORTATION_INPUT -> _isTransportationInputComplete.value = state
        }
        checkLeaderInfoNextBtnActive()
    }

    private fun checkLeaderInfoNextBtnActive() {
        /* _isLeaderInfoNextBtnActive.value = isNickNameInputComplete.value!!
                && isLocationInputComplete.value!! && isTransportationInputComplete.value!! */
        _isLeaderInfoNextBtnActive.value = isNickNameInputComplete.value!! && isTransportationInputComplete.value!!
    }

}