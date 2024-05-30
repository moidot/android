package com.moidot.moidot.presentation.main.group.space.member.vote.progress.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.repository.GroupPlaceRepository
import com.moidot.moidot.repository.GroupVoteRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MemberVoteProgressViewModel @Inject constructor(
    private val groupVoteRepository: GroupVoteRepository
) : ViewModel() {

    val voteId = MutableLiveData<Int>()
    val totalVoteNum = MutableLiveData<Int>()
    val isEnabledMultipleChoice = MutableLiveData<Boolean>(false)  // 복수 선// 택 여부
    val isAnonymous = MutableLiveData<Boolean>(false) // 익명 투표 여부
    private val _voteStatuses = MutableLiveData<List<ResponseVoteStatus.Data.VoteStatuses>>() // 투표 상태 정보
    val voteStatuses: LiveData<List<ResponseVoteStatus.Data.VoteStatuses>> = _voteStatuses

    private val _endAt = MutableLiveData<String>("none") // 종료 날짜
    val endAt: LiveData<String> = _endAt

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    fun loadVoteStatus(groupId: Int, userId:Int) {
        viewModelScope.launch {
            groupVoteRepository.getVoteStatus(groupId, userId).onSuccess {
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

}