package com.moidot.moidot.presentation.ui.main.group.create.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentInputLeaderInfoBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.create.viewmodel.CreateGroupViewModel

class InputLeaderInfoFragment : BaseFragment<FragmentInputLeaderInfoBinding>(R.layout.fragment_input_leader_info) {

    private val viewModel: CreateGroupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

}