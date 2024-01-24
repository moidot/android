package com.moidot.moidot.presentation.ui.main.group.participate.view

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.ActivityParticipateGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType
import com.moidot.moidot.presentation.ui.main.group.participate.viewmodel.ParticipateGroupViewModel
import com.moidot.moidot.presentation.util.PermissionUtil
import com.moidot.moidot.presentation.util.bottomsheet.BottomSheetLocationPicker
import com.moidot.moidot.presentation.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipateGroupActivity : BaseActivity<ActivityParticipateGroupBinding>(R.layout.activity_participate_group) {

    private val viewModel: ParticipateGroupViewModel by viewModels()
    lateinit var permissionUtil: PermissionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initPermissionUtil()
        initView()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initPermissionUtil() {
        permissionUtil = PermissionUtil(this, PermissionUtil.LOCATION_CHECK)
    }

    private fun initView() {
        setupNickNameInputView()
        setupTransportationPickerView()
        setTransportationSelectedType()
    }

    private fun setupNickNameInputView() {
        setNickNameFocusChangeListener()
        setNickNameTextChangeListener()
        setNickNameKeyListener()
    }

    private fun setNickNameFocusChangeListener() {
        binding.participateGroupEtvNickname.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                handleFocusChange(binding.participateGroupEtvNickname.text.toString())
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
        binding.participateGroupEtvNickname.addTextChangedListener {
            val name = it.toString()
            viewModel.setNickName(name)
            viewModel.setNickNameFieldActive(name)
            viewModel.updateInputInfoComplete(InputInfoType.NICKNAME_INPUT, name.isNotEmpty())
        }
    }

    private fun setNickNameKeyListener() {
        binding.participateGroupEtvNickname.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setNickNameFieldActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupTransportationPickerView() {
        binding.participateGroupComponentTransportationPicker.setLifecycleOwner(this)
    }

    fun onClickStartLocationPicker() {
        val locationPickerListener = object : BottomSheetLocationPicker.LocationPickerListener {
            override fun onSelectedItemListener(data: ResponseSearchPlace.Document) {
                viewModel.setLocationInfo(data)
                viewModel.updateInputInfoComplete(InputInfoType.LOCATION_INPUT, true)  // 위치 정보 입력 완료
            }
        }
        val bottomSheet = BottomSheetLocationPicker(locationPickerListener)
        bottomSheet.show(supportFragmentManager, "BottomSheetLocationPicker")
    }

    private fun setTransportationSelectedType() {
        binding.participateGroupComponentTransportationPicker.apply {
            selectedTransportationTypeTxt.observe(this@ParticipateGroupActivity) {
                if (it.isNotEmpty()) {
                    viewModel.setTransportationTypeTxt(it)
                    viewModel.updateInputInfoComplete(InputInfoType.TRANSPORTATION_INPUT, true)
                } else {
                    viewModel.updateInputInfoComplete(InputInfoType.TRANSPORTATION_INPUT, false)
                }
            }
        }
    }

    fun participateGroup() {
        if (viewModel.checkIsValidNickName()) {

        }
    }
}