package com.moidot.moidot.presentation.ui.main.group.create.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentInputGroupInfoBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.create.viewmodel.CreateGroupViewModel
import com.moidot.moidot.presentation.util.hideKeyboard

class InputGroupInfoFragment : BaseFragment<FragmentInputGroupInfoBinding>(R.layout.fragment_input_group_info) {

    private val viewModel: CreateGroupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun initView() {
        setGroupNameTextChangeListener()
        setGroupNameKeyListener()
    }

    private fun setGroupNameTextChangeListener() {
        binding.fgInputGroupInfoEtvGroupName.addTextChangedListener {
            val name = it.toString()
            viewModel.setGroupName(name)
            viewModel.setGroupNameFieldActive(name)
            viewModel.setGroupInfoNextBtnActive(name)
        }
    }

    private fun setGroupNameKeyListener() {
        binding.fgInputGroupInfoEtvGroupName.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setGroupNameFieldActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
}