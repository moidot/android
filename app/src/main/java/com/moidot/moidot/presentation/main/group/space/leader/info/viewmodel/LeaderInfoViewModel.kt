package com.moidot.moidot.presentation.main.group.space.leader.info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.repository.GroupInfoRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderInfoViewModel @Inject constructor(private val groupInfoRepository: GroupInfoRepository) : ViewModel() {

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _participantsByRegion = MutableLiveData<List<ResponseGroupInfo.Data.ParticipantsByRegion>>()
    val participantsByRegion: LiveData<List<ResponseGroupInfo.Data.ParticipantsByRegion>> = _participantsByRegion

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    private val _isGroupDeleteSuccess = MutableLiveData<Boolean>()
    val isGroupDeleteSuccess: LiveData<Boolean> = _isGroupDeleteSuccess

    private val _isMemberRemoveSuccess = MutableSingleLiveData<Boolean>()
    val isMemberRemoveSuccess: SingleLiveData<Boolean> = _isMemberRemoveSuccess

    fun getGroupInfo(groupId: Int) {
        viewModelScope.launch {
            groupInfoRepository.getGroupInfo(groupId).onSuccess {
                _groupName.value = it.data.name
                _participantsByRegion.value = it.data.participantsByRegion
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            groupInfoRepository.deleteGroup(groupId).onSuccess {
                if (it.code == 0) _isGroupDeleteSuccess.value = true
                else _showToastEvent.setValue(it.message.toString())
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    fun removeMember(groupId: Int, participateId: Int) {
        viewModelScope.launch {
            groupInfoRepository.removeMember(participateId).onSuccess {
                if (it.code == 0) getGroupInfo(groupId)
                else _showToastEvent.setValue(it.message.toString())
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }
}