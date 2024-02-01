package com.moidot.moidot.presentation.main.group.space.leader.info.edit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.repository.GroupInfoRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupNameViewModel @Inject constructor(private val groupInfoRepository: GroupInfoRepository) : ViewModel() {

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _isGroupNameFieldActive = MutableLiveData<Boolean>(false)
    val isGroupNameFieldActive: LiveData<Boolean> = _isGroupNameFieldActive

    private val _isGroupInfoNextBtnActive = MutableLiveData<Boolean>(false)
    val isGroupInfoNextBtnActive: LiveData<Boolean> = _isGroupInfoNextBtnActive

    val groupNameErrorMsg = MutableLiveData<String>()

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    val isEditGroupSuccess = MutableLiveData<Boolean>()

    fun editGroupName(groupId: Int, groupName: String) {
        viewModelScope.launch {
            groupInfoRepository.editGroupName(groupId, groupName).onSuccess {
                if (it.code == 0) isEditGroupSuccess.value = true
                else {
                    _showToastEvent.setValue(it.message.toString())
                }
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setGroupNameFieldActive(value: Any) {
        setFieldActive(_isGroupNameFieldActive, value)
    }

    fun setGroupInfoNextBtnActive(value: Any) {
        setFieldActive(_isGroupInfoNextBtnActive, value)
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

    private fun setFieldActive(field: MutableLiveData<Boolean>, value: Any) {
        field.value = when (value) {
            is String -> value.isNotEmpty()
            is Boolean -> value
            else -> throw IllegalArgumentException("Value Error!")
        }
    }
}