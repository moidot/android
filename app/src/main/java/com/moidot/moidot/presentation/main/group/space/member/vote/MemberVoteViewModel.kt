package com.moidot.moidot.presentation.main.group.space.member.vote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.repository.GroupVoteRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberVoteViewModel @Inject constructor(private val groupVoteRepository: GroupVoteRepository) : ViewModel() {

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    private val _groupVoteStatus = MutableLiveData<ResponseVoteStatus.Data>()
    val groupVoteStatus: LiveData<ResponseVoteStatus.Data> = _groupVoteStatus

    fun loadVoteStatus(groupId: Int) {
        viewModelScope.launch {
            groupVoteRepository.getVoteStatus(groupId).onSuccess {
                if (it.code == 0) _groupVoteStatus.value = it.data
                else _showToastEvent.setValue(it.message.toString())
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }
}