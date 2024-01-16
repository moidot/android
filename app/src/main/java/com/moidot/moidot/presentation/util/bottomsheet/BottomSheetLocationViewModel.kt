package com.moidot.moidot.presentation.util.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetLocationViewModel @Inject constructor() : ViewModel() {

    private val _searchWord = MutableLiveData<String>("")
    val searchWord: LiveData<String> = _searchWord

    private val _isSearchWordFieldActive = MutableLiveData<Boolean>(false)
    val isSearchWordFieldActive: LiveData<Boolean> = _isSearchWordFieldActive

    fun setSearchWord(word: String) {
        _searchWord.value = word
    }

    fun setSearchWordFieldActive(flag: Boolean) {
        _isSearchWordFieldActive.value = flag
    }

}