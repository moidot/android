package com.moidot.moidot.presentation.ui.onboard.view

import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentOnboardFirstBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.util.StatusBarColorUtil
import com.moidot.moidot.presentation.ui.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR

class OnboardFirstFragment : BaseFragment<FragmentOnboardFirstBinding>(R.layout.fragment_onboard_first) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
       initStatusBar()
    }

    private fun initStatusBar() {
        StatusBarColorUtil(requireActivity()).setStatusBar(R.color.gray800, LIGHT_ICON_COLOR)
    }

}