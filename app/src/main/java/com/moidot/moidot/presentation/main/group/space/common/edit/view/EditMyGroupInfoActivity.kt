package com.moidot.moidot.presentation.main.group.space.common.edit.view

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.ActivityEditMyGroupInfoBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.group.join.create.model.InputInfoType
import com.moidot.moidot.presentation.main.group.space.common.edit.viewmodel.EditMyGroupInfoViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.PermissionUtil
import com.moidot.moidot.util.PermissionUtil.Companion.LOCATION_CHECK
import com.moidot.moidot.util.bottomsheet.BottomSheetLocationPicker
import com.moidot.moidot.util.popup.edit.PopupEditDoneDialog
import com.moidot.moidot.util.view.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMyGroupInfoActivity : BaseActivity<ActivityEditMyGroupInfoBinding>(R.layout.activity_edit_my_group_info) {

    lateinit var permissionUtil: PermissionUtil
    private val groupId by lazy { intent.getIntExtra(GROUP_ID, -1) }
    private val groupName by lazy { intent.getStringExtra(GROUP_NAME) ?: "" }

    private val viewModel: EditMyGroupInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initBinding()
        initPermissionUtil()
        loadPrevUserData()
        setupNickNameInputView()
        setTransportationSelectedTypeView()
        setupObservers()
    }

    private fun initView() {
        binding.editMyGroupInfoIvBack.setOnClickListener { finish() }
        binding.editMyGroupInfoTvGroupName.text = groupName
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initPermissionUtil(){
        permissionUtil = PermissionUtil(this, LOCATION_CHECK)
    }

    private fun setupObservers() {
        viewModel.prevUserGroupInfo.observe(this) {
            initPrevTransportationView(it.transportation)
        }
        viewModel.isNickNameDuplicated.observe(this) {
            if (it) {
                viewModel.nickNameErrorMsg.value = getString(R.string.participate_group_nickname_duplicate_error)
                viewModel.updateInputInfoComplete(InputInfoType.NICKNAME_INPUT, false)
            }
        }
        viewModel.isEditSuccess.observe(this) {// 수정 성공
            if (it) showEditPopupDialog(viewModel.checkUpdatedInfo())
        }
        viewModel.showToastEvent.observe(this) {// 에러 메세지 출력
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPrevUserData() {
        viewModel.loadPrevInfo(groupId)
    }

    // 닉네임 설정
    private fun setupNickNameInputView() {
        setNickNameFocusChangeListener()
        setNickNameTextChangeListener()
        setNickNameKeyListener()
    }

    private fun setNickNameFocusChangeListener() {
        binding.editMyGroupInfoEtvNickname.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (binding.editMyGroupInfoEtvNickname.text.isNotEmpty()) {
                    viewModel.setNickNameFieldActive(true)
                }
                viewModel.nickNameErrorMsg.value = ""
            }
        }
    }

    private fun setNickNameTextChangeListener() {
        var hasPrevData = true // 뷰 활성화를 막기위한 체크 State
        binding.editMyGroupInfoEtvNickname.addTextChangedListener {
            val name = it.toString()
            viewModel.newNickName.value = name
            if(!hasPrevData) viewModel.setNickNameFieldActive(name)
            viewModel.updateInputInfoComplete(InputInfoType.NICKNAME_INPUT, name.isNotEmpty())
            hasPrevData = false
        }
    }

    private fun setNickNameKeyListener() {
        binding.editMyGroupInfoEtvNickname.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setNickNameFieldActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    // 출발위치
    fun onClickStartLocationPicker() {
        val locationPickerListener = object : BottomSheetLocationPicker.LocationPickerListener {
            override fun onSelectedItemListener(data: ResponseSearchPlace.Document) {
                viewModel.newLocationInfo.value = data
            }
        }
        val bottomSheet = BottomSheetLocationPicker(locationPickerListener)
        bottomSheet.show(supportFragmentManager, "BottomSheetLocationPicker")
    }

    // 이동수단 선택
    private fun setTransportationSelectedTypeView() {
        binding.editMyGroupInfoComponentTransportationPicker.setLifecycleOwner(this)
        binding.editMyGroupInfoComponentTransportationPicker.apply {
            selectedtransportationTypeTxt.observe(this@EditMyGroupInfoActivity) {
                if (it.isNotEmpty()) {
                    viewModel.newTransportation.value = it
                    viewModel.updateInputInfoComplete(InputInfoType.TRANSPORTATION_INPUT, true)
                } else {
                    viewModel.updateInputInfoComplete(InputInfoType.TRANSPORTATION_INPUT, false)
                }
            }
        }
    }

    private fun initPrevTransportationView(transportationType: String) {
        if (transportationType == "PUBLIC") {
            binding.editMyGroupInfoComponentTransportationPicker.apply {
                isPublicSelected.value = true
                selectedtransportationTypeTxt.value = "PUBLIC"
                viewModel.newTransportation.value = "PUBLIC"
            }
        } else {
            binding.editMyGroupInfoComponentTransportationPicker.apply {
                isCarSelected.value = true
                selectedtransportationTypeTxt.value = "PERSONAL"
                viewModel.newTransportation.value = "PERSONAL"
            }
        }
    }

    fun onClickEditMyGroupInfoListener() {
        if (viewModel.checkIsValidNickName()) {
            viewModel.checkNickNameDuplicate(groupId)
        }
    }

    private fun showEditPopupDialog(updatedInfo: List<Boolean>) {
        val nickname = if (updatedInfo[0]) viewModel.newNickName.value else null
        val location = if (updatedInfo[1]) viewModel.newLocationInfo.value!!.placeName else null
        val transportation = if (updatedInfo[2]) viewModel.newTransportation.value else null

        val dialog = PopupEditDoneDialog(nickname, location, transportation) { finish() }
        dialog.show(supportFragmentManager, "update_popup")
    }
}