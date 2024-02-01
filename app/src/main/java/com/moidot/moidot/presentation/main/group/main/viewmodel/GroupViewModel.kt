package com.moidot.moidot.presentation.main.group.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.repository.GroupRepository
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
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

    private val _myGroupList = MutableLiveData<List<ResponseParticipateGroup.Data>>(emptyList())
    val myGroupList: LiveData<List<ResponseParticipateGroup.Data>> = _myGroupList

    val isGroupListEmpty = MutableLiveData<Boolean>(false)

    private val _showToastEvent = MutableSingleLiveData<String>()
    val showToastEvent: SingleLiveData<String> = _showToastEvent

    fun loadMyGroupList() {
        viewModelScope.launch {
            groupRepository.getMyGroupList().onSuccess {
                if (it.code == 0) {
                    _myGroupList.value = it.data
                    isGroupListEmpty.value = it.data.isEmpty()
                } else _showToastEvent.setValue(it.message.toString())
            }.onFailure {
                _showToastEvent.setValue(it.message.toString())
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