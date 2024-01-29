package com.moidot.moidot.presentation.ui.main.group.myGroup.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.join.create.view.CreateGroupActivity
import com.moidot.moidot.presentation.ui.main.group.myGroup.viewmodel.GroupViewModel
import com.moidot.moidot.presentation.ui.main.group.myGroup.adater.MyGroupAdapter
import com.moidot.moidot.presentation.ui.main.group.space.leader.LeaderSpaceActivity
import com.moidot.moidot.presentation.util.Constant.GROUP_ID
import com.moidot.moidot.presentation.util.Constant.GROUP_NAME
import com.moidot.moidot.presentation.util.Constant.GROUP_LEADER_NAME
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR
import com.moidot.moidot.presentation.util.VerticalSpaceItemDecoration
import com.moidot.moidot.presentation.util.dpToPx
import com.moidot.moidot.presentation.util.hideKeyboard
import com.moidot.moidot.presentation.util.popup.PopupPickerDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding>(R.layout.fragment_group) {

    private lateinit var myGroupAdapter: MyGroupAdapter
    private val viewModel: GroupViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStatusBarState(STATUS_BAR_LIGHT)
    }

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
        initAdapter()
        setSearchTextChangeListener()
        setSearchDoneListener()
    }

    private fun initAdapter() {
        myGroupAdapter = MyGroupAdapter(::onGroupItemClickListener)
        binding.fgGroupRvMyGroup.apply {
            adapter = myGroupAdapter
            itemAnimator = null
            addItemDecoration(VerticalSpaceItemDecoration(8.dpToPx(this.context)))
        }
    }

    private fun onGroupItemClickListener(groupId: Int, groupName: String, groupAdminName: String) {
        Intent(requireContext(), LeaderSpaceActivity::class.java).apply { // TODO 추후 변수 추가 시 값 대응 예정
            this.putExtra(GROUP_ID, groupId)
            this.putExtra(GROUP_NAME, groupName)
            this.putExtra(GROUP_LEADER_NAME, groupAdminName)
            startActivity(this)
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
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { // TODO 검색 완료
                viewModel.setSearchWord(it.toString())
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

    private fun setupObservers() {
        setupCurrentFilterTxt()
        setupGroupRecyclerView()
    }

    private fun setupCurrentFilterTxt() {
        viewModel.currentFilterTxt.observe(viewLifecycleOwner) {
            binding.fgGroupFilterTxt.text = it
        }
    }

    private fun setupGroupRecyclerView() {
        viewModel.myGroupList.observe(viewLifecycleOwner) {
            myGroupAdapter.submitList(it)
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