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

}