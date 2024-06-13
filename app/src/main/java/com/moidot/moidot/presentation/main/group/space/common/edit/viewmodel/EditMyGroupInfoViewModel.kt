package com.moidot.moidot.presentation.main.group.space.common.edit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.request.RequestEditGroupInfo
import com.moidot.moidot.data.remote.response.ResponseGroupUserInfo
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType
import com.moidot.moidot.repository.GroupRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMyGroupInfoViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
) : ViewModel() {

    // 기존 정보
    private val prevUserGroupInfo = MutableLiveData<ResponseGroupUserInfo.Data>()

    // 닉네임
    val newNickName = MutableLiveData<String>()
    val isNickNameFieldActive = MutableLiveData<Boolean>(false)
    val nickNameErrorMsg = MutableLiveData<String>()
    val isNickNameInputComplete = MutableLiveData<Boolean>(true)
    val isNickNameDuplicated = MutableLiveData<Boolean>(false)

    // 출발위치
    val newLocationInfo = MutableLiveData<ResponseSearchPlace.Document>()

    // 이동수단
    val newTransportation = MutableLiveData<String>()
    val isTransportationInputComplete = MutableLiveData<Boolean>(true)

    // 버튼 활성화
    val isBtnActive = MutableLiveData<Boolean>(true)

    // 수정 성공
    private val _isEditSuccess = MutableSingleLiveData<Boolean>()
    val isEditSuccess: SingleLiveData<Boolean> = _isEditSuccess

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    fun loadPrevInfo(groupId: Int) {
        viewModelScope.launch {
            groupRepository.getUserInfo(groupId).onSuccess {
                if (it.code == 0) {
                    it.data.apply {
                        prevUserGroupInfo.value = this
                        newNickName.value = userName
                        newLocationInfo.value = ResponseSearchPlace.Document(
                            placeName = locationName,
                            roadAddressName = locationName,
                            addressName = locationName,
                            longitude = longitude,
                            latitude = latitude,
                            isFavorite = false
                        )
                        newTransportation.value = transportation
                    }
                }
            }
        }
    }

    fun setNickNameFieldActive(value: Any) {
        isNickNameFieldActive.value = when (value) {
            is String -> value.isNotEmpty()
            is Boolean -> value
            else -> throw IllegalArgumentException("Value Error!")
        }
    }

    fun updateInputInfoComplete(infoType: InputInfoType, state: Boolean) { // 입력 상태 갱신
        when (infoType) {
            InputInfoType.NICKNAME_INPUT -> isNickNameInputComplete.value = state
            InputInfoType.TRANSPORTATION_INPUT -> isTransportationInputComplete.value = state
            InputInfoType.LOCATION_INPUT -> {}
        }
        checkEditMyGroupInfoBtnActive()
    }

    private fun checkEditMyGroupInfoBtnActive() {
        isBtnActive.value = isNickNameInputComplete.value!! && isTransportationInputComplete.value!!
    }

    fun checkIsValidNickName(): Boolean {
        val nickname = newNickName.value!!
        val specialChars = "!#$%&'()*+,/:;=?@[]_~-|{} " // 사용 가능한 특수문자
        val disallowedChars = nickname.filter { !it.isLetterOrDigit() && it !in specialChars }

        val errorMsg = when {
            disallowedChars.isNotEmpty() -> "'${disallowedChars}'는(은) 사용할 수 없습니다."
            nickname.all { !it.isLetterOrDigit() } -> "특수 문자만으로는 닉네임을 만들 수 없습니다."
            else -> ""
        }

        return updateNickNameInfoStates(errorMsg)
    }

    fun checkNickNameDuplicate(groupId: Int) {
        viewModelScope.launch {
            groupRepository.checkNicknameDuplication(groupId, newNickName.value!!).onSuccess {
                if (it.code == 0) {
                    if (it.data.duplicated) {
                        isNickNameDuplicated.value = true
                    } else {
                        editMyGroupInfo()
                    }
                } else {
                    _showToastEvent.setValue(it.message.toString())
                }
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    private fun updateNickNameInfoStates(errorMsg: String): Boolean {
        nickNameErrorMsg.value = errorMsg

        val isValid = errorMsg.isEmpty()
        updateInputInfoComplete(InputInfoType.NICKNAME_INPUT, isValid)

        return isValid
    }

    fun editMyGroupInfo() {
        val requestEditGroupInfo = RequestEditGroupInfo(
            participateId = prevUserGroupInfo.value!!.participationId,
            userName = newNickName.value!!,
            locationName = newLocationInfo.value!!.placeName,
            longitude = newLocationInfo.value!!.longitude,
            latitude = newLocationInfo.value!!.latitude,
            transportationType = newTransportation.value!!
        )
        viewModelScope.launch {
            groupRepository.editMyGroupInfo(requestEditGroupInfo).onSuccess {
                if (it.code == 0) _isEditSuccess.setValue(true)
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    // 업데이트 된 유저 정보 확인
    fun checkUpdatedInfo(): List<Boolean> {
        val updatedInfo = mutableListOf(false, false, false) // 닉네임, 출발위치, 이동수단
        prevUserGroupInfo.value?.apply {
            if (userName != newNickName.value) updatedInfo[0] = true
            if (locationName != newLocationInfo.value?.placeName) updatedInfo[1] = true
            if (transportation != newTransportation.value) updatedInfo[2] = true
        }
        return updatedInfo
    }
}