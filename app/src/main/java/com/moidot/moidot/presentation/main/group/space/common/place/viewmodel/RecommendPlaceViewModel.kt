package com.moidot.moidot.presentation.main.group.space.common.place.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseRecommendPlace
import com.moidot.moidot.repository.GroupPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendPlaceViewModel @Inject constructor(
    private val groupPlaceRepository: GroupPlaceRepository
) : ViewModel() {

    private val _recommendPlaces = MutableLiveData<List<ResponseRecommendPlace.Data>>()
    val recommendPlaces : LiveData<List<ResponseRecommendPlace.Data>> = _recommendPlaces

    fun getRecommendPlace(x: Double, y: Double, local: String, keyword: String) {
        viewModelScope.launch {
            groupPlaceRepository.recommendPlace(x,y,local,keyword).onSuccess {
                if(it.code == 0) _recommendPlaces.value = it.data
            }
        }
    }

}