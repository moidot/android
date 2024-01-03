package com.moidot.moidot.presentation.ui.onboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardFragmentViewModel @Inject constructor() : ViewModel() {

    private var _currentPos = MutableLiveData<Int>()
    val currentPos: LiveData<Int> = _currentPos

    fun setCurrentPos(posValue: Int) {
        _currentPos.value = posValue
    }
}