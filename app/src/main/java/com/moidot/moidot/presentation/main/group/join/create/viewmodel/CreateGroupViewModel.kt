package com.moidot.moidot.presentation.main.group.join.create.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.request.RequestCreateGroup
import com.moidot.moidot.data.remote.response.ResponseCreateGroup
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.repository.GroupRepository
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType.NICKNAME_INPUT
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType.LOCATION_INPUT
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType.TRANSPORTATION_INPUT
import com.moidot.moidot.util.TimeUtil
import com.moidot.moidot.presentation.main.group.join.create.view.InputLeaderInfoFragment.Companion.ERROR_GROUP_IDX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

    private val _groupId = MutableLiveData<Int>()
    val groupId: LiveData<Int> = _groupId

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

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
    private val isNickNameInputComplete = MutableLiveData<Boolean>(false) // 닉네임

    private val _isLocationInputComplete = MutableLiveData<Boolean>(false) // 장소
    val isLocationInputComplete: LiveData<Boolean> = _isLocationInputComplete

    private val isTransportationInputComplete = MutableLiveData<Boolean>(false) // 교통 수단

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

    // 그룹 생성 서버 통신 결과
    private val _createGroupResult = MutableLiveData<ResponseCreateGroup>()
    val createGroupResult: LiveData<ResponseCreateGroup> = _createGroupResult

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setNickName(name: String) {
        _nickname.value = name
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

    fun checkIsValidNickName(): Boolean {
        val nickname = _nickname.value!!
        val specialChars = "!#$%&'()*+,/:;=?@[]_~-|{} " // 사용 가능한 특수문자
        val disallowedChars = nickname.filter { !it.isLetterOrDigit() && it !in specialChars }

        val errorMsg = when {
            disallowedChars.isNotEmpty() -> "'${disallowedChars}'는(은) 사용할 수 없습니다."
            nickname.all { !it.isLetterOrDigit() } -> "특수 문자만으로는 닉네임을 만들 수 없습니다."
            else -> ""
        }

        return updateNickNameInfoStates(errorMsg)
    }

    private fun updateGroupInfoStates(errorMsg: String): Boolean {
        groupNameErrorMsg.value = errorMsg

        val isValid = errorMsg.isEmpty()
        _isGroupInfoNextBtnActive.value = isValid

        return isValid
    }

    private fun updateNickNameInfoStates(errorMsg: String): Boolean {
        nickNameErrorMsg.value = errorMsg

        val isValid = errorMsg.isEmpty()
        updateInputInfoComplete(NICKNAME_INPUT, isValid)

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
            NICKNAME_INPUT -> isNickNameInputComplete.value = state
            LOCATION_INPUT -> _isLocationInputComplete.value = state
            TRANSPORTATION_INPUT -> isTransportationInputComplete.value = state
        }
        checkLeaderInfoNextBtnActive()
    }

    private fun checkLeaderInfoNextBtnActive() {
        _isLeaderInfoNextBtnActive.value = isNickNameInputComplete.value!!
                && isLocationInputComplete.value!! && isTransportationInputComplete.value!!
    }

    fun createGroup() {
        val requestCreateGroup = prepareCreateGroupRequest()
        viewModelScope.launch {
            groupRepository.createGroup(requestCreateGroup).onSuccess {
                _groupId.value = it.data.groupId
            }.onFailure {
                _groupId.value = ERROR_GROUP_IDX
            }
        }
    }

    private fun prepareCreateGroupRequest(): RequestCreateGroup {
        return RequestCreateGroup(
            date = TimeUtil.getCurrentDateTime(),
            name = groupName.value!!,
            userName = nickname.value!!,
            locationName = locationInfo.value!!.addressName ?: "",
            latitude = locationInfo.value!!.latitude,
            longitude = locationInfo.value!!.longitude,
            transportationType = transportationTypeTxt.value!!
        )
    }

}