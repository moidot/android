package com.moidot.moidot.presentation.ui.main.group.create.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CreateGroupViewModel @Inject constructor() : ViewModel() {

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _isGroupNameFieldActive = MutableLiveData<Boolean>(false)
    val isGroupNameFieldActive: LiveData<Boolean> = _isGroupNameFieldActive

    private val _isGroupInfoNextBtnActive = MutableLiveData<Boolean>(false)
    val isGroupInfoNextBtnActive: LiveData<Boolean> = _isGroupInfoNextBtnActive

    val groupNameErrorMsg = MutableLiveData<String>()

    private val _isNavigationToLeaderInfoAllowed = MutableLiveData<Boolean>(false)
    val isNavigationToLeaderInfoAllowed: LiveData<Boolean> = _isNavigationToLeaderInfoAllowed

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setGroupNameFieldActive(value: Any) {
        setFieldActive(_isGroupNameFieldActive, value)
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

    fun checkIsValidGroupName() {
        val groupName = groupName.value!!
        val specialChars = "!#$%&'()*+,/:;=?@[]_~-|{}" // 사용 가능한 특수문자
        val disallowedChars = groupName.filter { !it.isLetterOrDigit() && it !in specialChars }

        val errorMsg = when {
            disallowedChars.isNotEmpty() -> "'${disallowedChars}'는(은) 사용할 수 없습니다."
            groupName.all { !it.isLetterOrDigit() } -> "특수 문자만으로는 모임명을 만들 수 없습니다."
            else -> ""
        }

        groupNameErrorMsg.value = errorMsg
        _isGroupInfoNextBtnActive.value = errorMsg.isEmpty()
        _isNavigationToLeaderInfoAllowed.value = errorMsg.isEmpty()
    }

}