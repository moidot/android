package com.moidot.moidot.presentation.ui.main.group

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseMyGroupList
import com.moidot.moidot.databinding.FragmentGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.adater.MyGroupAdapter
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR
import com.moidot.moidot.presentation.util.hideKeyboard
import com.moidot.moidot.presentation.util.popup.PopupPickerDialog

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
        myGroupAdapter = MyGroupAdapter()
        val tempList = mutableListOf<ResponseMyGroupList.Data>()
        tempList.add(ResponseMyGroupList.Data(groupId = 0, groupName = "모이닷 스페이스", groupDate = "2022.08.21"))
        tempList.add(ResponseMyGroupList.Data(groupId = 1, groupName = "네이버", groupDate = "2022.04.12"))
        tempList.add(ResponseMyGroupList.Data(groupId = 2, groupName = "오늘의집", groupDate = "2024.01.02"))
        tempList.add(ResponseMyGroupList.Data(groupId = 3, groupName = "ABC초콜릿", groupDate = "2024.01.01"))
        tempList.add(ResponseMyGroupList.Data(groupId = 4, groupName = "1등하자", groupDate = "2022.03.12"))
        myGroupAdapter.submitList(tempList.toList())
        binding.fgGroupRvMyGroup.adapter = myGroupAdapter
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
        PopupPickerDialog(requireContext(), "모임 정렬", filters).show()
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