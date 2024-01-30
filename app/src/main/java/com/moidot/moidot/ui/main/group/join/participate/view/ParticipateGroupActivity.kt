package com.moidot.moidot.ui.main.group.join.participate.view

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.ActivityParticipateGroupBinding
import com.moidot.moidot.ui.base.BaseActivity
import com.moidot.moidot.ui.main.group.join.create.model.InputInfoType.NICKNAME_INPUT
import com.moidot.moidot.ui.main.group.join.create.model.InputInfoType.TRANSPORTATION_INPUT
import com.moidot.moidot.ui.main.group.join.create.model.InputInfoType.LOCATION_INPUT
import com.moidot.moidot.ui.main.group.join.participate.viewmodel.ParticipateGroupViewModel
import com.moidot.moidot.ui.main.group.space.member.MemberSpaceActivity
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.PermissionUtil
import com.moidot.moidot.util.bottomsheet.BottomSheetLocationPicker
import com.moidot.moidot.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipateGroupActivity : BaseActivity<ActivityParticipateGroupBinding>(R.layout.activity_participate_group) {

    private val groupId by lazy { intent.getIntExtra(GROUP_ID, 0) }
    private val groupName by lazy { intent.getStringExtra(GROUP_NAME) }

    private val viewModel: ParticipateGroupViewModel by viewModels()
    lateinit var permissionUtil: PermissionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initPermissionUtil()
        initView()
        setupObserver()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initPermissionUtil() {
        permissionUtil = PermissionUtil(this, PermissionUtil.LOCATION_CHECK)
    }

    private fun initView() {
        setupGroupInfoView()
        setupNickNameInputView()
        setupTransportationPickerView()
        setTransportationSelectedType()
    }

    private fun setupGroupInfoView() {
        viewModel.groupId.value = groupId
        viewModel.groupName.value = groupName
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
            viewModel.updateInputInfoComplete(NICKNAME_INPUT, name.isNotEmpty())
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
                viewModel.updateInputInfoComplete(LOCATION_INPUT, true)  // 위치 정보 입력 완료
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
                    viewModel.updateInputInfoComplete(TRANSPORTATION_INPUT, true)
                } else {
                    viewModel.updateInputInfoComplete(TRANSPORTATION_INPUT, false)
                }
            }
        }
    }

    private fun setupObserver() {
        setDuplicateNicknameView()
        checkParticipateDoneState()
    }

    private fun setDuplicateNicknameView() {
        viewModel.nicknameDuplicated.observe(this) {
            if (it) {
                viewModel.nickNameErrorMsg.value = getString(R.string.participate_group_nickname_duplicate_error)
                viewModel.updateInputInfoComplete(NICKNAME_INPUT, false)
            }
        }
    }

    private fun checkParticipateDoneState() {
        viewModel.isParticipateGroupSuccess.observe(this) {
            if (it) {
                moveToMoidotSpace()
            } else {
                Toast.makeText(this, getString(R.string.network_error_msg), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToMoidotSpace() {
        Intent(this, MemberSpaceActivity::class.java).apply {
            putExtra(GROUP_ID, viewModel.groupId.value)
            putExtra(GROUP_NAME, viewModel.groupName.value)
            startActivity(this)
        }
        finish()
    }

    fun participateGroup() {
        if (viewModel.checkIsValidNickName()) {
            viewModel.checkNickNameDuplicate()
        }
    }
}