package com.moidot.moidot.presentation.main.group.space.leader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaderSpaceViewModel @Inject constructor() : ViewModel() {

    private val _groupId = MutableLiveData<Int>(0)
    val groupId: LiveData<Int> = _groupId

    private val _groupName = MutableLiveData<String>("")
    val groupName: LiveData<String> = _groupName

    private val _groupParticipates = MutableLiveData<Int>(1)
    val groupParticipates: LiveData<Int> = _groupParticipates

    fun setGroupId(id: Int) {
        _groupId.value = id
    }

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setGroupParticipates(count: Int) {
        _groupParticipates.value = count
    }

}