package com.moidot.moidot.presentation.ui.main.group.create.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.FragmentInputLeaderInfoBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.create.viewmodel.CreateGroupViewModel
import com.moidot.moidot.presentation.util.bottomsheet.BottomSheetLocationPicker

class InputLeaderInfoFragment : BaseFragment<FragmentInputLeaderInfoBinding>(R.layout.fragment_input_leader_info) {

    private val viewModel: CreateGroupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
    }

    private fun initView() {
        setupTransportationPickerView()
    }

    private fun setupTransportationPickerView() {
        binding.fgInputLeaderInfoComponentTransportationPicker.setLifecycleOwner(this)
    }

    fun onClickStartLocationPicker() {
        val locationPickerListener = object : BottomSheetLocationPicker.LocationPickerListener {
            override fun onSelectedItemListener(data: ResponseSearchPlace.Document) {
                viewModel.setLocationInfo(data) // TODO 위치 텍스트 반영
            }
        }
        val bottomSheet = BottomSheetLocationPicker(locationPickerListener)
        bottomSheet.show(childFragmentManager, "")
    }

}