package com.moidot.moidot.presentation.ui.main.group.create.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CreateGroupViewModel @Inject constructor() : ViewModel() {

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _isGroupNameFieldActive = MutableLiveData<Boolean>()
    val isGroupNameFieldActive: LiveData<Boolean> = _isGroupNameFieldActive
    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setGroupNameFieldActive(name: String) {
        _isGroupNameFieldActive.value = name.isNotEmpty()
    }

}