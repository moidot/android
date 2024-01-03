package com.moidot.moidot.presentation.ui.onboard.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.moidot.moidot.R
import com.moidot.moidot.data.data.OnboardItem
import com.moidot.moidot.databinding.FragmentOnboardSecondBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.onboard.adapter.OnBoardingAdapter
import com.moidot.moidot.presentation.ui.onboard.viewmodel.OnboardFragmentViewModel
import com.moidot.moidot.presentation.util.StatusBarColorUtil
import com.moidot.moidot.presentation.util.StatusBarColorUtil.Companion.DARK_ICON_COLOR

class OnboardSecondFragment : BaseFragment<FragmentOnboardSecondBinding>(R.layout.fragment_onboard_second) {

    private val viewModel: OnboardFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
        setupObservers()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.activity = (activity as OnboardActivity)
    }

    private fun initView() {
        initStatusBar()
        setOnBoardVpAdapter()
        setOnBoardVpEvent()
    }

    private fun initStatusBar() {
        StatusBarColorUtil(requireActivity()).setStatusBar(R.color.white, DARK_ICON_COLOR)
    }

    private fun setOnBoardItems():MutableList<OnboardItem> {
        return mutableListOf<OnboardItem>().apply {
            add(OnboardItem(ContextCompat.getDrawable(requireContext(),R.drawable.img_onboard_second_door)!!,resources.getString(R.string.onboard_second_title_social_login),resources.getString(R.string.onboard_second_content_social_login)))
            add(OnboardItem(ContextCompat.getDrawable(requireContext(),R.drawable.img_login_logo)!!,resources.getString(R.string.onboard_second_title_moidot_space),resources.getString(R.string.onboard_second_content_moidot_space)))
            add(OnboardItem(ContextCompat.getDrawable(requireContext(),R.drawable.img_login_logo)!!,resources.getString(R.string.onboard_second_title_invite),resources.getString(R.string.onboard_second_content_invite)))
            add(OnboardItem(ContextCompat.getDrawable(requireContext(),R.drawable.img_login_logo)!!,resources.getString(R.string.onboard_second_title_recommend_place),resources.getString(R.string.onboard_second_content_recommend_place)))
            add(OnboardItem(ContextCompat.getDrawable(requireContext(),R.drawable.img_login_logo)!!,resources.getString(R.string.onboard_second_title_vote_place),resources.getString(R.string.onboard_second_content_vote_place)))
        }
    }

    private fun setOnBoardVpAdapter() {
        val onBoardingAdapter = OnBoardingAdapter()
        onBoardingAdapter.itemList = setOnBoardItems()
        binding.fgOnboardSecondVp.adapter = onBoardingAdapter
    }

    private fun setOnBoardVpEvent() {
        binding.fgOnboardSecondVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setCurrentPos(position)
            }
        })
    }

    private fun setupObservers() {
        viewModel.currentPos.observe(viewLifecycleOwner) {
            changeChipCheckedState(it)
        }
    }

    private fun changeChipCheckedState(currentPos: Int) {
        binding.fgOnboardSecondChipGroupIndicator.children.forEachIndexed { index, view ->
            val chip = view as Chip
            val fontId = if (index == currentPos) R.font.pretendard_bold else R.font.pretendard_regular
            chip.isChecked = index == currentPos
            chip.typeface = Typeface.create(ResourcesCompat.getFont(requireContext(), fontId), Typeface.BOLD.takeIf { index == currentPos } ?: Typeface.NORMAL)
        }
    }

    fun moveToNext() {
        val nextPos = viewModel.currentPos.value!!.plus(1)
        if (nextPos < 5) binding.fgOnboardSecondVp.currentItem = nextPos else (activity as OnboardActivity).exitOnboard()
    }
}