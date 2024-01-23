package com.moidot.moidot.presentation.util.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moidot.moidot.data.local.entity.PlaceEntity
import com.moidot.moidot.data.local.toDocument
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BottomSheetLocationViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val _searchWord = MutableLiveData<String>("")
    val searchWord: LiveData<String> = _searchWord

    private val _isSearchWordFieldActive = MutableLiveData<Boolean>(false)
    val isSearchWordFieldActive: LiveData<Boolean> = _isSearchWordFieldActive

    private val _searchResults = MutableLiveData<List<ResponseSearchPlace.Document>>()
    val searchResults: LiveData<List<ResponseSearchPlace.Document>> = _searchResults

    private val _currentLocationResults = MutableLiveData<List<ResponseSearchPlace.Document>>()
    val currentLocationResults: LiveData<List<ResponseSearchPlace.Document>> = _currentLocationResults

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

    fun addFavorite(place: PlaceEntity) = viewModelScope.launch(Dispatchers.IO) {
        locationRepository.insertPlace(place)
    }

    fun deleteFavorite(place: PlaceEntity) = viewModelScope.launch(Dispatchers.IO) {
        locationRepository.deletePlace(place)
    }

    suspend fun getSavedFavoritePlaces(): List<ResponseSearchPlace.Document> {
        val savedFavorites = mutableListOf<ResponseSearchPlace.Document>()
        withContext(Dispatchers.IO) {
            locationRepository.getFavoritePlaces().forEach {
                savedFavorites.add(it.toDocument())
            }
        }
        return savedFavorites
    }

    fun getCurrentLocations(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            locationRepository.getAddressFromCoord(longitude, latitude).onSuccess { response ->
                _currentLocationResults.value = response.documents.map { result ->
                    result.toDocument(longitude, latitude)
                }
            }
        }
    }


}