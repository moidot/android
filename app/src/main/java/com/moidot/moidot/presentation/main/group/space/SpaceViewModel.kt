package com.moidot.moidot.presentation.main.group.space

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseGroupUserInfo
import com.moidot.moidot.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpaceViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

    private val _groupId = MutableLiveData<Int>(0)
    val groupId: LiveData<Int> = _groupId

    private val _groupName = MutableLiveData<String>("")
    val groupName: LiveData<String> = _groupName

    private val _groupParticipates = MutableLiveData<Int>(1)
    val groupParticipates: LiveData<Int> = _groupParticipates

    private val _userInfo = MutableLiveData<ResponseGroupUserInfo.Data>()
    val userInfo : LiveData<ResponseGroupUserInfo.Data> = _userInfo

    private val userName = MutableLiveData<String>("")

    fun loadUserInfo() {
        viewModelScope.launch {
            groupRepository.getUserInfo(_groupId.value!!).onSuccess {
                if (it.code == 0) {
                    _userInfo.value = it.data
                    userName.value = it.data.userName
                }
            }
        }
    }

    fun setGroupId(id: Int) {
        _groupId.value = id
    }

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    fun setGroupParticipates(count: Int) {
        _groupParticipates.value = count
    }

    fun getUserName():String {
        return userName.value!!
    }

}