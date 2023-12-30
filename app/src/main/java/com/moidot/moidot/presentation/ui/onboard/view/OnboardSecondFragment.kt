package com.moidot.moidot.presentation.ui.onboard.view

import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentOnboardSecondBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.util.StatusBarColorUtil
import com.moidot.moidot.presentation.ui.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR

class OnboardSecondFragment : BaseFragment<FragmentOnboardSecondBinding>(R.layout.fragment_onboard_second) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initStatusBar()
    }

    private fun initStatusBar() {
        StatusBarColorUtil(requireActivity()).setStatusBar(R.color.white, DARK_ICON_COLOR)
    }
}