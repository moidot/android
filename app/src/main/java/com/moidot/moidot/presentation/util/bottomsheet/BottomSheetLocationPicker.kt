package com.moidot.moidot.presentation.util.bottomsheet

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.BottomSheetLocationPickerBinding
import com.moidot.moidot.presentation.ui.base.BaseBottomSheetDialogFragment
import com.moidot.moidot.presentation.util.CustomSnackBar
import com.moidot.moidot.presentation.util.VerticalSpaceItemDecoration
import com.moidot.moidot.presentation.util.dpToPx
import com.moidot.moidot.presentation.util.hideKeyboard
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
        binding.bottomSheetLocationPickerRvPlace.apply {
            adapter = locationAdapter
            addItemDecoration(VerticalSpaceItemDecoration(8.dpToPx(this.context)))
        }
    }

    private fun onItemSelectListener(data: ResponseSearchPlace.Document) {
        CoroutineScope(Dispatchers.Main).launch {
            onLocationSelectListener.onSelectedItemListener(data)
            delay(500)
            dismiss()
        }
    }

    private fun onFavoriteSelectListener(position:Int, data: ResponseSearchPlace.Document) {
        if (data.isFavorite) {
            data.isFavorite =false
        } else {
            CustomSnackBar.makeSnackBar(binding.root, SNACK_BAR_FAVORITE_MSG).show()
            data.isFavorite = true
        }
        locationAdapter.notifyItemChanged(position)
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) {
            locationAdapter.submitList(it)
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

    companion object {
        const val SNACK_BAR_FAVORITE_MSG = "장소를 즐겨찾기에 저장했어요!"
    }
}