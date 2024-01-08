package com.moidot.moidot.presentation.ui.main.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor() : ViewModel() {

    private val _searchWord = MutableLiveData<String>()
    val searchWord: LiveData<String> = _searchWord

    private val _isSearchActive = MutableLiveData<Boolean>()
    val isSearchActive: LiveData<Boolean> = _isSearchActive

    fun setSearchWord(word: String) {
         _searchWord.value = word
    }

    fun setSearchActive(word:String) {
        _isSearchActive.value = word.isNotEmpty()
    }

    fun setSearchActive(isActive: Boolean) {
        _isSearchActive.value = isActive
    }
}