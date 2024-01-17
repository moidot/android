package com.moidot.moidot.presentation.util.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetLocationViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val _searchWord = MutableLiveData<String>("")
    val searchWord: LiveData<String> = _searchWord

    private val _isSearchWordFieldActive = MutableLiveData<Boolean>(false)
    val isSearchWordFieldActive: LiveData<Boolean> = _isSearchWordFieldActive

    private val _searchResults = MutableLiveData<List<ResponseSearchPlace.Document>>()
    val searchResults: LiveData<List<ResponseSearchPlace.Document>> = _searchResults

    fun setSearchWord(word: String) {
        _searchWord.value = word
    }

    fun setSearchWordFieldActive(flag: Boolean) {
        _isSearchWordFieldActive.value = flag
    }


    fun searchPlace() {
        viewModelScope.launch {
            locationRepository.searchPlace(searchWord.value!!).onSuccess {
                _searchResults.value = it.places
            }
        }
    }

}