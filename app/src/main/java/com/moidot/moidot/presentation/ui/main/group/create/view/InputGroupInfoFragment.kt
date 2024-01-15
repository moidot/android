package com.moidot.moidot.presentation.ui.main.group.create.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentInputGroupInfoBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.create.viewmodel.CreateGroupViewModel

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
    }

    private fun setGroupNameTextChangeListener() {
        binding.fgInputGroupInfoEtvGroupName.addTextChangedListener {
            val name = it.toString()
            viewModel.setGroupName(name)
            viewModel.setGroupNameFieldActive(name)
        }
    }
}