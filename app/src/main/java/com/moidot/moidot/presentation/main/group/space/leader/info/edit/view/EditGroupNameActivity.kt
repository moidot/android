package com.moidot.moidot.presentation.main.group.space.leader.info.edit.view

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityEditGroupNameBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.group.space.leader.info.edit.viewmodel.EditGroupNameViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.SpannableTxt
import com.moidot.moidot.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditGroupNameActivity : BaseActivity<ActivityEditGroupNameBinding>(R.layout.activity_edit_group_name) {

    private val groupId by lazy { intent.getIntExtra(GROUP_ID, 0) }
    private val groupName by lazy { intent.getStringExtra(GROUP_NAME) ?: "" }
    private val viewModel: EditGroupNameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        setupObservers()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initView() {
        setPrevGroupNameView()
        setGroupNameFocusChangeListener()
        setGroupNameTextChangeListener()
        setGroupNameKeyListener()
    }

    private fun setPrevGroupNameView() {
        val content = getString(R.string.edit_group_name_new_group_name_content).format(groupName)
        val spannableTxt = SpannableTxt(this).applySpannableStyles(content, groupName, R.color.orange500, R.style.b2_bold_14)
        binding.editGroupNameTvContent.text = spannableTxt
    }

    private fun setGroupNameFocusChangeListener() {
        binding.editGroupNameEtvGroupName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                handleFocusChange(binding.editGroupNameEtvGroupName.text.toString())
            }
        }
    }

    private fun handleFocusChange(groupName: String) {
        if (groupName.isNotEmpty()) {
            viewModel.setGroupNameFieldActive(true)
        }
        viewModel.groupNameErrorMsg.value = ""
    }

    private fun setGroupNameTextChangeListener() {
        binding.editGroupNameEtvGroupName.addTextChangedListener {
            val name = it.toString()
            viewModel.setGroupName(name)
            viewModel.setGroupNameFieldActive(name)
            viewModel.setGroupInfoNextBtnActive(name)
        }
    }

    private fun setGroupNameKeyListener() {
        binding.editGroupNameEtvGroupName.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setGroupNameFieldActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupObservers() {
        viewModel.showToastEvent.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isEditGroupSuccess.observe(this) {
            if (it) finish()
        }
    }

    fun editGroupName() {
        if (viewModel.checkIsValidGroupName()) {
            viewModel.editGroupName(groupId, viewModel.groupName.value!!)
        }
    }

}