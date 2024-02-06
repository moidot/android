package com.moidot.moidot.presentation.main.group.space.common.viewmodel

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

    private val _bestRegions = MutableLiveData<List<ResponseBestRegion.Data>>()
    val bestRegions: LiveData<List<ResponseBestRegion.Data>> = _bestRegions

    fun getBestRegions(groupId: Int) {
        viewModelScope.launch {
            groupPlaceRepository.bestRegions(groupId).onSuccess {
                if (it.code == 0) _bestRegions.value = it.data
            }.onFailure {

            }
        }
    }

}