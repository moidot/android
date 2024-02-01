package com.moidot.moidot.presentation.main.group.join.create.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.FragmentInputLeaderInfoBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType.NICKNAME_INPUT
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType.LOCATION_INPUT
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType.TRANSPORTATION_INPUT
import com.moidot.moidot.presentation.main.group.join.create.viewmodel.CreateGroupViewModel
import com.moidot.moidot.presentation.main.group.space.leader.LeaderSpaceActivity
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.bottomsheet.BottomSheetLocationPicker
import com.moidot.moidot.util.component.TransportationPickerComponent.Companion.TYPE_PERSONAL
import com.moidot.moidot.util.component.TransportationPickerComponent.Companion.TYPE_PUBLIC
import com.moidot.moidot.util.hideKeyboard

class InputLeaderInfoFragment : BaseFragment<FragmentInputLeaderInfoBinding>(R.layout.fragment_input_leader_info) {

    private val viewModel: CreateGroupViewModel by activityViewModels()

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
        checkPreviousInputData()
        setupNickNameInputView()
        setupTransportationPickerView()
    }

    // 입력 하다가 뒤로 가기 버튼을 눌렀을 때
    // 기존에 입력된 데이터를 유지시키기 위해 사용하는 함수
    private fun checkPreviousInputData() {
        binding.fgInputLeaderInfoEtvNickname.setText(viewModel.nickname.value)
        when (viewModel.transportationTypeTxt.value) {
            TYPE_PERSONAL -> binding.fgInputLeaderInfoComponentTransportationPicker.isCarSelected.value = true
            TYPE_PUBLIC -> binding.fgInputLeaderInfoComponentTransportationPicker.isPublicSelected.value = true
        }
    }

    private fun setupNickNameInputView() {
        setNickNameFocusChangeListener()
        setNickNameTextChangeListener()
        setNickNameKeyListener()
    }

    private fun setNickNameFocusChangeListener() {
        binding.fgInputLeaderInfoEtvNickname.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                handleFocusChange(binding.fgInputLeaderInfoEtvNickname.text.toString())
            }
        }
    }

    private fun handleFocusChange(nickName: String) {
        if (nickName.isNotEmpty()) {
            viewModel.setNickNameFieldActive(true)
        }
        viewModel.nickNameErrorMsg.value = ""
    }

    private fun setNickNameTextChangeListener() {
        binding.fgInputLeaderInfoEtvNickname.addTextChangedListener {
            val name = it.toString()
            viewModel.setNickName(name)
            viewModel.setNickNameFieldActive(name)
            viewModel.updateInputInfoComplete(NICKNAME_INPUT, name.isNotEmpty())
        }
    }

    private fun setNickNameKeyListener() {
        binding.fgInputLeaderInfoEtvNickname.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setNickNameFieldActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupTransportationPickerView() {
        binding.fgInputLeaderInfoComponentTransportationPicker.setLifecycleOwner(this)
    }

    fun onClickStartLocationPicker() {
        val locationPickerListener = object : BottomSheetLocationPicker.LocationPickerListener {
            override fun onSelectedItemListener(data: ResponseSearchPlace.Document) {
                viewModel.setLocationInfo(data)
                viewModel.updateInputInfoComplete(LOCATION_INPUT, true)  // 위치 정보 입력 완료
            }
        }
        val bottomSheet = BottomSheetLocationPicker(locationPickerListener)
        bottomSheet.show(childFragmentManager, "BottomSheetLocationPicker")
    }

    private fun setupObservers() {
        setTransportationSelectedType()
        moveToMoidotSpace()
    }

    private fun moveToMoidotSpace() {
        viewModel.groupId.observe(viewLifecycleOwner) {
            if (it != ERROR_GROUP_IDX) {
                Intent(requireContext(), LeaderSpaceActivity::class.java).apply {
                    putExtra(GROUP_ID, it)
                    putExtra(GROUP_NAME, viewModel.groupName.value)
                    startActivity(this)
                }
                (activity as CreateGroupActivity).finish()
            } else {
                Toast.makeText(requireContext(), getString(R.string.network_error_msg), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setTransportationSelectedType() {
        binding.fgInputLeaderInfoComponentTransportationPicker.apply {
            selectedTransportationTypeTxt.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    viewModel.setTransportationTypeTxt(it)
                    viewModel.updateInputInfoComplete(TRANSPORTATION_INPUT, true)
                } else {
                    viewModel.updateInputInfoComplete(TRANSPORTATION_INPUT, false)
                }
            }
        }
    }

    fun createGroup() {
        if (viewModel.checkIsValidNickName()) {
            viewModel.createGroup()
        }
    }

    fun goBack() {
        findNavController().navigateUp()
    }

    companion object {
        const val ERROR_GROUP_IDX = -1
    }
}