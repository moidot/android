package com.moidot.moidot.presentation.main.group.space.member.vote.before

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.repository.GroupPlaceRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberVoteBeforeViewModel @Inject constructor(private val groupPlaceRepository: GroupPlaceRepository) : ViewModel() {

    private val _bestRegions = MutableLiveData<List<ResponseBestRegion.Data>>()
    val bestRegions: LiveData<List<ResponseBestRegion.Data>> = _bestRegions

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading = _isLoading

    fun loadBestRegions(groupId: Int) {
        viewModelScope.launch {
            groupPlaceRepository.bestRegions(groupId).onSuccess {
                if (it.code == 0) _bestRegions.value = it.data
                else _showToastEvent.setValue(it.message.toString())
                _isLoading.value = false
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
                _isLoading.value = false
            }
        }
    }
}