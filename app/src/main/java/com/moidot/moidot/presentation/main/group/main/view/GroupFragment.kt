package com.moidot.moidot.presentation.main.group.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.databinding.FragmentGroupBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.join.create.view.CreateGroupActivity
import com.moidot.moidot.presentation.main.group.main.viewmodel.GroupViewModel
import com.moidot.moidot.presentation.main.group.main.adater.MyGroupAdapter
import com.moidot.moidot.presentation.main.group.space.leader.LeaderSpaceActivity
import com.moidot.moidot.presentation.main.group.space.member.MemberSpaceActivity
import com.moidot.moidot.presentation.main.mypage.setting.view.SettingActivity
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_MEMBER_COUNT
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.StatusBarColorUtil
import com.moidot.moidot.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR
import com.moidot.moidot.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR
import com.moidot.moidot.util.view.hideKeyboard
import com.moidot.moidot.util.popup.picker.PopupPickerDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding>(R.layout.fragment_group) {

    private lateinit var myGroupAdapter: MyGroupAdapter
    private val viewModel: GroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarState(STATUS_BAR_LIGHT)
        initBinding()
        initView()
        setupObservers()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
    }

    private fun initView() {
        initAdapter()
        setSearchTextChangeListener()
        setSearchDoneListener()
    }

    private fun initAdapter() {
        myGroupAdapter = MyGroupAdapter(::onGroupItemClickListener)
        binding.fgGroupRvMyGroup.apply {
            adapter = myGroupAdapter
            itemAnimator = null
        }
    }

    private fun onGroupItemClickListener(data: ResponseParticipateGroup.Data) {
        data.apply {
            val packageClass = if (isAdmin) LeaderSpaceActivity::class.java else MemberSpaceActivity::class.java
            Intent(requireContext(), packageClass).apply {
                this.putExtra(GROUP_ID, groupId)
                this.putExtra(GROUP_NAME, groupName)
                this.putExtra(GROUP_MEMBER_COUNT, groupParticipates)
                startActivity(this)
            }
        }
    }

    private fun setSearchTextChangeListener() {
        binding.fgGroupEtvSearch.addTextChangedListener {
            val word = it.toString()
            viewModel.setSearchWord(word)
            viewModel.setSearchActive(word)
        }
    }

    private fun setSearchDoneListener() {
        binding.fgGroupEtvSearch.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setSearchWord(it.text.toString())
                viewModel.searchWordWithFilter()
                viewModel.setSearchActive(false)
                it.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun onCancelSearch() {
        binding.fgGroupEtvSearch.apply {
            setText("")
            viewModel.loadMyGroupList()
            this.hideKeyboard()
        }
    }

    fun onSearchFilterListener() {
        val filters = listOf("가나다순", "최신순", "오래된순")
        val onItemSelectedListener = object : PopupPickerDialog.PopupPickerDialogListener {
            override fun onSelectedTextListener(selectedTxt: String) {
                viewModel.setCurrentFilterTxt(selectedTxt)
            }
        }
        PopupPickerDialog(requireContext(), "모임 정렬", filters, viewModel.getCurrentFilterTxt(), onItemSelectedListener).show()
    }

    fun moveToCreateGroup() {
        startActivity(Intent(requireContext(), CreateGroupActivity::class.java))
    }

    fun moveToSetting() {
        startActivity(Intent(requireContext(), SettingActivity::class.java))
    }

    private fun setupObservers() {
        setupCurrentFilterTxt()
        setupGroupRecyclerView()
        setupToastEventObserver()
    }

    private fun setupCurrentFilterTxt() {
        viewModel.currentFilterTxt.observe(viewLifecycleOwner) {
            binding.fgGroupFilterTxt.text = it
            viewModel.searchWordWithFilter()
        }
    }

    private fun setupGroupRecyclerView() {
        viewModel.myGroupList.observe(viewLifecycleOwner) {
            myGroupAdapter.updateItems(it)
        }
    }

    private fun setupToastEventObserver() {
        viewModel.showToastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyGroupList()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        setStatusBarState(STATUS_BAR_DARK)
    }

    private fun setStatusBarState(state: String) {
        val statusBarColorUtil = StatusBarColorUtil(requireActivity())
        when (state) {
            STATUS_BAR_LIGHT -> statusBarColorUtil.setStatusBar(R.color.gray800, LIGHT_ICON_COLOR)
            STATUS_BAR_DARK -> statusBarColorUtil.setStatusBar(R.color.white, DARK_ICON_COLOR)
        }
    }

    companion object {
        const val STATUS_BAR_LIGHT = "STATUS_BAR_LIGHT"
        const val STATUS_BAR_DARK = "STATUS_BAR_DARK"
    }
}