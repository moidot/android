package com.moidot.moidot.presentation.ui.main.group.myGroup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

    private val _searchWord = MutableLiveData<String>()
    val searchWord: LiveData<String> = _searchWord

    private val _isSearchActive = MutableLiveData<Boolean>()
    val isSearchActive: LiveData<Boolean> = _isSearchActive

    private val _currentFilterTxt = MutableLiveData<String>("최신순")
    val currentFilterTxt: LiveData<String> = _currentFilterTxt

    private val _myGroupList = MutableLiveData<List<ResponseParticipateGroup.Data>>()
    val myGroupList: LiveData<List<ResponseParticipateGroup.Data>> = _myGroupList

    fun loadMyGroupList() {
        viewModelScope.launch {
            groupRepository.getMyGroupList().onSuccess {
                _myGroupList.value = it.data
            }
        }
    }

    fun setSearchWord(word: String) {
        _searchWord.value = word
    }

    fun setSearchActive(word: String) {
        _isSearchActive.value = word.isNotEmpty()
    }

    fun setSearchActive(flag: Boolean) {
        _isSearchActive.value = flag
    }

    fun getCurrentFilterTxt(): String {
        return _currentFilterTxt.value!!
    }

    fun setCurrentFilterTxt(filterTxt: String) {
        _currentFilterTxt.value = filterTxt
    }

}