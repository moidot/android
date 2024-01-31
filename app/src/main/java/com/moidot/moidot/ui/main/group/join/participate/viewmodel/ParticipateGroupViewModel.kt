package com.moidot.moidot.ui.main.group.join.participate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.request.RequestPostParticipateGroup
import com.moidot.moidot.data.remote.response.ResponsePostParticipateGroup
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.repository.GroupRepository
import com.moidot.moidot.ui.main.group.join.create.model.InputInfoType
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateGroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

    val groupId = MutableLiveData<Int>()
    val groupName = MutableLiveData<String>("")

    private val _nickname = MutableLiveData<String>("")
    val nickname: LiveData<String> = _nickname

    private val _transportationTypeTxt = MutableLiveData<String>()
    val transportationTypeTxt: LiveData<String> = _transportationTypeTxt

    private val _locationInfo = MutableLiveData<ResponseSearchPlace.Document>()
    val locationInfo: LiveData<ResponseSearchPlace.Document> = _locationInfo

    private val _nicknameDuplicated = MutableLiveData<Boolean>(false)
    val nicknameDuplicated: LiveData<Boolean> = _nicknameDuplicated

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

    private val _responsePostParticipateGroup = MutableLiveData<ResponsePostParticipateGroup>()
    val responsePostParticipateGroup: LiveData<ResponsePostParticipateGroup> = _responsePostParticipateGroup

    private val _isParticipateGroupSuccess = MutableLiveData<Boolean>()
    val isParticipateGroupSuccess: LiveData<Boolean> = _isParticipateGroupSuccess

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    fun setNickName(name: String) {
        _nickname.value = name
    }

    fun setLocationInfo(data: ResponseSearchPlace.Document) {
        _locationInfo.value = data
    }

    fun setTransportationTypeTxt(transportationType: String) {
        _transportationTypeTxt.value = transportationType
    }

    fun setNickNameFieldActive(value: Any) {
        setFieldActive(_isNickNameFieldActive, value)
    }

    private fun setFieldActive(field: MutableLiveData<Boolean>, value: Any) {
        field.value = when (value) {
            is String -> value.isNotEmpty()
            is Boolean -> value
            else -> throw IllegalArgumentException("Value Error!")
        }
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

    private fun updateNickNameInfoStates(errorMsg: String): Boolean {
        nickNameErrorMsg.value = errorMsg

        val isValid = errorMsg.isEmpty()
        updateInputInfoComplete(InputInfoType.NICKNAME_INPUT, isValid)

        return isValid
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

    fun checkNickNameDuplicate() {
        viewModelScope.launch {
            groupRepository.checkNicknameDuplication(groupId.value!!, nickname.value!!).onSuccess {
                if (it.data.duplicated) {
                    _nicknameDuplicated.value = true
                } else {
                    participateGroup()
                }
            }
        }
    }

    private fun participateGroup() {
        viewModelScope.launch {
            val requestParticipateGroup = prepareParticipateGroupRequest()
            groupRepository.participateGroup(requestParticipateGroup).onSuccess {
                if (it.code == 0) {
                    _responsePostParticipateGroup.value = it
                    _isParticipateGroupSuccess.value = true
                }
                else {
                    _showToastEvent.setValue(it.message.toString())
                }
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    private fun prepareParticipateGroupRequest(): RequestPostParticipateGroup {
        return RequestPostParticipateGroup(
            groupId = groupId.value!!,
            userName = nickname.value!!,
            locationName = locationInfo.value!!.addressName ?: "",
            latitude = locationInfo.value!!.latitude,
            longitude = locationInfo.value!!.longitude,
            transportationType = transportationTypeTxt.value!!
        )
    }

}