package com.moidot.moidot.presentation.ui.onboard.view

import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentOnboardFirstBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR

class OnboardFirstFragment : BaseFragment<FragmentOnboardFirstBinding>(R.layout.fragment_onboard_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        initStatusBar()
    }

    private fun initStatusBar() {
        StatusBarColorUtil(requireActivity()).setStatusBar(R.color.gray800, LIGHT_ICON_COLOR)
    }

    fun moveToOnboardSecond() {
        (activity as OnboardActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.onboard_fcv, OnboardSecondFragment())
            .commit()
    }

    fun exitOnboard() {
        (activity as OnboardActivity).finish()
    }
}