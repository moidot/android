package com.moidot.moidot.presentation.main.group.space.leader.vote.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateVoteViewModel @Inject constructor() : ViewModel() {

    private val _hasEndTime = MutableLiveData<Boolean>(false)
    val hasEndTime: LiveData<Boolean> = _hasEndTime

    private val _dateInputDone = MutableLiveData<Boolean>(false)
    val dateInputDone: LiveData<Boolean> = _dateInputDone

    private val _timeInputDone = MutableLiveData<Boolean>(false)
    val timeInputDone: LiveData<Boolean> = _timeInputDone

    private val _multipleSelectionsState = MutableLiveData<Boolean>(false)
    val multipleSelectionsState: LiveData<Boolean> = _multipleSelectionsState

    private val _anonymousVoteState = MutableLiveData<Boolean>(false)
    val anonymousVoteState: LiveData<Boolean> = _anonymousVoteState

    fun setHasEndTime(flag: Boolean) {
        _hasEndTime.value = flag
    }

    fun setDateInputDone(flag: Boolean) {
        _dateInputDone.value = flag
    }

    fun setTimeInputDone(flag: Boolean) {
        _timeInputDone.value = flag
    }

    fun setMultipleSelectionsCheckState(flag: Boolean) {
        _multipleSelectionsState.value = flag
    }

    fun setAnonymousVoteState(flag: Boolean) {
        _anonymousVoteState.value = flag
    }
}