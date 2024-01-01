package com.moidot.moidot.presentation.ui.onboard.view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentOnboardFirstBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.LIGHT_ICON_COLOR

class OnboardFirstFragment : BaseFragment<FragmentOnboardFirstBinding>(R.layout.fragment_onboard_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initStatusBar()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.activity = (activity as OnboardActivity)
    }

    private fun initStatusBar() {
        StatusBarColorUtil(requireActivity()).setStatusBar(R.color.gray800, LIGHT_ICON_COLOR)
    }

    fun moveToOnboardSecond() {
        findNavController().navigate(OnboardFirstFragmentDirections.actionOnboardFirstFragmentToOnboardSecondFragment())
    }

}