package com.moidot.moidot.presentation.util.bottomsheet

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.moidot.moidot.R
import com.moidot.moidot.databinding.BottomSheetLocationPickerBinding
import com.moidot.moidot.presentation.ui.base.BaseBottomSheetDialogFragment
import com.moidot.moidot.presentation.util.hideKeyboard

class BottomSheetLocationPicker : BaseBottomSheetDialogFragment<BottomSheetLocationPickerBinding>(R.layout.bottom_sheet_location_picker) {

    val searchWord = MutableLiveData<String>("")
    val isSearchWordFieldActive = MutableLiveData<Boolean>(false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initView() {
        setSearchTextFocusChangeListener()
        setSearchTextChangeListener()
        setSearchKeyListener()
    }

    private fun setSearchTextFocusChangeListener() {
        binding.bottomSheetLocationPickerEtvSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (searchWord.value!!.isNotEmpty()) {
                    isSearchWordFieldActive.value = true
                }
            }
        }
    }

    private fun setSearchTextChangeListener() {
        binding.bottomSheetLocationPickerEtvSearch.addTextChangedListener {
            val location = it.toString()
            isSearchWordFieldActive.value = location.isNotEmpty()
            searchWord.value = location
        }
    }

    private fun setSearchKeyListener() {
        binding.bottomSheetLocationPickerEtvSearch.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { // TODO 검색 진행
                isSearchWordFieldActive.value = false
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun onClickSearchListener() { // TODO 검색 진행
        isSearchWordFieldActive.value = false
        binding.bottomSheetLocationPickerEtvSearch.apply {
            hideKeyboard()
            clearFocus()
        }
    }

}