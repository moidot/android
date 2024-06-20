package com.moidot.moidot.presentation.main.group.space.common.place.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.repository.GroupPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupPlaceViewModel @Inject constructor(private val groupPlaceRepository: GroupPlaceRepository) : ViewModel() {

    val isMapInitialized = MutableLiveData<Boolean>(false)

    private val _currentPos = MutableLiveData<Int>()
    val currentPos: LiveData<Int> = _currentPos

    private val _bestRegions = MutableLiveData<List<ResponseBestRegion.Data>>()
    val bestRegions: LiveData<List<ResponseBestRegion.Data>> = _bestRegions

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading = _isLoading

    fun setCurrentPos(posValue: Int) {
        _currentPos.value = posValue
    }

    fun getBestRegions(groupId: Int) {
        viewModelScope.launch {
            groupPlaceRepository.bestRegions(groupId).onSuccess {
                if (it.code == 0) _bestRegions.value = it.data
                _isLoading.value = false
            }.onFailure {
                _isLoading.value = false
            }
        }
    }
}