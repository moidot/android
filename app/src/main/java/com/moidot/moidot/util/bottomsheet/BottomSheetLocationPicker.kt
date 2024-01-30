package com.moidot.moidot.util.bottomsheet

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.moidot.moidot.R
import com.moidot.moidot.data.local.toPlaceEntity
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.BottomSheetLocationPickerBinding
import com.moidot.moidot.ui.base.BaseBottomSheetDialogFragment
import com.moidot.moidot.ui.main.group.join.create.view.CreateGroupActivity
import com.moidot.moidot.ui.main.group.join.participate.view.ParticipateGroupActivity
import com.moidot.moidot.util.CustomSnackBar
import com.moidot.moidot.util.VerticalSpaceItemDecoration
import com.moidot.moidot.util.dpToPx
import com.moidot.moidot.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomSheetLocationPicker(private val onLocationSelectListener: LocationPickerListener) : BaseBottomSheetDialogFragment<BottomSheetLocationPickerBinding>(R.layout.bottom_sheet_location_picker) {

    private val viewModel: BottomSheetLocationViewModel by viewModels()
    private val locationAdapter: BottomSheetLocationAdapter by lazy { BottomSheetLocationAdapter(::onItemSelectListener, ::onFavoriteSelectListener) }

    interface LocationPickerListener {
        fun onSelectedItemListener(data: ResponseSearchPlace.Document)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
        setupObservers()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
    }

    private fun initView() {
        setSearchTextFocusChangeListener()
        setSearchTextChangeListener()
        setSearchKeyListener()
        initAdapter()
    }

    private fun setSearchTextFocusChangeListener() {
        binding.bottomSheetLocationPickerEtvSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (binding.bottomSheetLocationPickerEtvSearch.text.isNotEmpty()) {
                    viewModel.setSearchWordFieldActive(true)
                }
            }
        }
    }

    private fun setSearchTextChangeListener() {
        binding.bottomSheetLocationPickerEtvSearch.addTextChangedListener {
            val word = it.toString()
            viewModel.setSearchWord(word)
            viewModel.setSearchWordFieldActive(word.isNotEmpty())
        }
    }

    private fun setSearchKeyListener() {
        binding.bottomSheetLocationPickerEtvSearch.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchPlace()
                viewModel.setSearchWordFieldActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initAdapter() {
        loadSavedFavoritePlace()
        binding.bottomSheetLocationPickerRvPlace.apply {
            adapter = locationAdapter
            itemAnimator = null
            addItemDecoration(VerticalSpaceItemDecoration(8.dpToPx(this.context)))
        }
    }

    private fun loadSavedFavoritePlace() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getSavedFavoritePlaces().apply {
                locationAdapter.setPlaceItems(this)
                locationAdapter.savedFavorites = this
            }
        }
    }

    private fun updateSavedFavoritePlace() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getSavedFavoritePlaces().apply {
                locationAdapter.savedFavorites = this
            }
        }
    }

    private fun onItemSelectListener(data: ResponseSearchPlace.Document) {
        CoroutineScope(Dispatchers.Main).launch {
            onLocationSelectListener.onSelectedItemListener(data)
            delay(500)
            dismiss()
        }
    }

    private fun onFavoriteSelectListener(position: Int, data: ResponseSearchPlace.Document) {
        if (data.isFavorite) {
            viewModel.deleteFavorite(data.toPlaceEntity())
            data.isFavorite = false
        } else {
            CustomSnackBar.makeSnackBar(binding.root, SNACK_BAR_FAVORITE_MSG).show()
            data.isFavorite = true
            viewModel.addFavorite(data.toPlaceEntity())
        }

        locationAdapter.notifyItemChanged(position)
    }

    private fun setupObservers() {
        updateViewFromSearch() // 키워드 기준 장소 검색
        updateViewFromCurrentLocation() // 현재위치 좌표 기준 장소 반환
    }

    private fun updateViewFromSearch() {
        viewModel.searchResults.observe(viewLifecycleOwner) {
            updateSavedFavoritePlace()
            val updatedResults = it.map { result ->  // isFavorite 빼고 나머지 요소가 같은지 비교
                locationAdapter.savedFavorites.find { savedPlace ->
                    savedPlace.copy(isFavorite = result.isFavorite) == result
                } ?: result
            }
            locationAdapter.setPlaceItems(updatedResults)
        }
    }

    private fun updateViewFromCurrentLocation() {
        viewModel.currentLocationResults.observe(viewLifecycleOwner) {
            val currentLocationName = it[0].placeName
            binding.bottomSheetLocationPickerEtvSearch.apply {
                setText(currentLocationName)
                clearFocus()
            }
            viewModel.setSearchWord(currentLocationName)
            viewModel.setSearchWordFieldActive(true)
        }
    }

    fun onClickSearchListener() {
        viewModel.searchPlace()
        viewModel.setSearchWordFieldActive(false)
        binding.bottomSheetLocationPickerEtvSearch.apply {
            hideKeyboard()
            clearFocus()
        }
    }

    fun checkLocationPermission() {
        when (val currentActivity = activity) {
            is CreateGroupActivity -> currentActivity.permissionUtil.requestLocationPermission { getMyCurrentLocation() }
            is ParticipateGroupActivity -> currentActivity.permissionUtil.requestLocationPermission { getMyCurrentLocation() }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                viewModel.getCurrentLocations(it.longitude, it.latitude)
            } ?: run {
                Toast.makeText(requireContext(), CURRENT_LOCATION_ERROR_MSG, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), CURRENT_LOCATION_ERROR_MSG, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val SNACK_BAR_FAVORITE_MSG = "장소를 즐겨찾기에 저장했어요!"
        const val CURRENT_LOCATION_ERROR_MSG = "현재 위치를 불러올 수 없습니다."
        const val CREATE_GROUP = "CREATE_GROUP"
        const val PARTICIPATE_GROUP = "PARTICIPATE_GROUP"
    }
}