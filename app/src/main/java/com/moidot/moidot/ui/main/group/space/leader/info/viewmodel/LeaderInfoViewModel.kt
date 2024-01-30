package com.moidot.moidot.ui.main.group.space.leader.info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.repository.GroupInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderInfoViewModel @Inject constructor(private val groupInfoRepository: GroupInfoRepository) : ViewModel() {

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> = _groupName

    private val _participantsByRegion = MutableLiveData<List<ResponseGroupInfo.Data.ParticipantsByRegion>>()
    val participantsByRegion: LiveData<List<ResponseGroupInfo.Data.ParticipantsByRegion>> = _participantsByRegion

    fun getGroupInfo(groupId: Int) {
        viewModelScope.launch {
            groupInfoRepository.getGroupInfo(groupId).onSuccess {
                _groupName.value = it.data.name
                _participantsByRegion.value = it.data.participantsByRegion
            }
        }
    }
}