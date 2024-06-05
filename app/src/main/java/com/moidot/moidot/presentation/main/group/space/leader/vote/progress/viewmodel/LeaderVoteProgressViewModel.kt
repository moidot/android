package com.moidot.moidot.presentation.main.group.space.leader.vote.progress.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseUsersVotePlaceInfo
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.repository.GroupVoteRepository
import com.moidot.moidot.repository.UserRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LeaderVoteProgressViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupVoteRepository: GroupVoteRepository
) : ViewModel() {

    val voteId = MutableLiveData<Int>()
    val totalVoteNum = MutableLiveData<Int>()
    val isEnabledMultipleChoice = MutableLiveData<Boolean>(false)  // 복수 선// 택 여부
    val isAnonymous = MutableLiveData<Boolean>(false) // 익명 투표 여부
    private val _voteStatuses = MutableLiveData<List<ResponseVoteStatus.Data.VoteStatuses>>() // 투표 상태 정보
    val voteStatuses: LiveData<List<ResponseVoteStatus.Data.VoteStatuses>> = _voteStatuses

    private val _votePlaceUsersInfo = MutableLiveData<List<ResponseUsersVotePlaceInfo.Data.VoteParticipation>>()
    val votePlaceUsersInfo = _votePlaceUsersInfo

    private val _endAt = MutableLiveData<String>("none") // 종료 날짜
    val endAt: LiveData<String> = _endAt

    private val _isVoteEnd = MutableSingleLiveData<Boolean>(false) // 투표 종료 여부
    val isVoteEnd: SingleLiveData<Boolean> = _isVoteEnd

    private val _isVoteDone = MutableSingleLiveData<Boolean>(false) // 투표 완료 여부
    val isVoteDone: SingleLiveData<Boolean> = _isVoteDone

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    fun loadVoteStatus(groupId: Int) {
        viewModelScope.launch {
            groupVoteRepository.getVoteStatus(groupId, userRepository.getUserInfo().userId).onSuccess {
                if (it.code == 0) {
                    voteId.value = it.data.voteId
                    totalVoteNum.value = it.data.totalVoteNum
                    isEnabledMultipleChoice.value = it.data.isEnabledMultipleChoice
                    isAnonymous.value = it.data.isAnonymous
                    _endAt.value = it.data.endAt?.let { input ->
                        if (input != "none") {
                            val dateTime = LocalDateTime.parse(input)
                            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                            dateTime.format(formatter) + " 종료"
                        } else input
                    } ?: ""
                    _voteStatuses.value = it.data.voteStatuses
                } else _showToastEvent.setValue(it.message.toString())
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
            }
        }
    }

    fun endVote(groupId: Int) {
        viewModelScope.launch {
            groupVoteRepository.endVote(groupId).onSuccess {
                if (it.code == 0) _isVoteEnd.setValue(true)
            }
        }
    }

    fun votePlace(groupId: Int, bestPlaceIds: List<Int>) {
        viewModelScope.launch {
            groupVoteRepository.votePlace(groupId, bestPlaceIds).onSuccess {
                if (it.code == 0) _isVoteDone.setValue(true)
            }
        }
    }

    val userVotePlaceName = MutableLiveData<String>("")
    fun getUsersVotePlaceInfo(groupId: Int, bestPlaceId: Int, bestPlaceName: String) {
        userVotePlaceName.value = bestPlaceName
        viewModelScope.launch {
            groupVoteRepository.getUsersPlaceVoteInfo(groupId, bestPlaceId).onSuccess {
                if (it.code == 0) _votePlaceUsersInfo.value = it.data.voteParticipations
            }
        }
    }
}