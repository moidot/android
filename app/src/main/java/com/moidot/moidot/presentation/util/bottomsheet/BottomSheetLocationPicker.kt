package com.moidot.moidot.presentation.util.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.BottomSheetLocationPickerBinding
import com.moidot.moidot.presentation.ui.base.BaseBottomSheetDialogFragment
import com.moidot.moidot.presentation.util.VerticalSpaceItemDecoration
import com.moidot.moidot.presentation.util.dpToPx
import com.moidot.moidot.presentation.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetLocationPicker : BaseBottomSheetDialogFragment<BottomSheetLocationPickerBinding>(R.layout.bottom_sheet_location_picker) {

    private val viewModel: BottomSheetLocationViewModel by viewModels()
    private val locationAdapter: BottomSheetLocationAdapter by lazy { BottomSheetLocationAdapter(::onItemSelected) }

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

    private fun onItemSelected(data: ResponseSearchPlace.Document) {
        Log.d("kite", data.toString())
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

}