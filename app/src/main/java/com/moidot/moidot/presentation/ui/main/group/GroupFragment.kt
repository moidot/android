package com.moidot.moidot.presentation.ui.main.group

import android.content.Context
import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.adater.MyGroupAdapter
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR

class GroupFragment : BaseFragment<FragmentGroupBinding>(R.layout.fragment_group) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStatusBarState(STATUS_BAR_LIGHT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        val myGroupAdapter = MyGroupAdapter()
        myGroupAdapter.submitList(listOf<String>("모이닷 팀 스페이스", "여기어때", "야놀자", "오늘의집", "모이닷 팀 스페이스", "오늘의집", "오늘의집", "네이버", "카카오", "다음", "쿠팡"))
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