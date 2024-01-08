package com.moidot.moidot.presentation.ui.main.group

import android.content.Context
import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseMyGroupList
import com.moidot.moidot.databinding.FragmentGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.adater.MyGroupAdapter
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR

class GroupFragment : BaseFragment<FragmentGroupBinding>(R.layout.fragment_group) {

    private lateinit var myGroupAdapter: MyGroupAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStatusBarState(STATUS_BAR_LIGHT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
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