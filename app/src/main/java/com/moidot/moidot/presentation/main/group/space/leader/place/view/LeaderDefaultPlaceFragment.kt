package com.moidot.moidot.presentation.main.group.space.leader.place.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderDefaultPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class LeaderDefaultPlaceFragment : BaseFragment<FragmentLeaderDefaultPlaceBinding>(R.layout.fragment_leader_default_place) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initBottomSheetBehavior()
    }

    /** slideOffset: 0은 완전히 닫힌 상태 1은 완전 펼쳐진 상태이다. slideOffset가
     * 절반 높이인 0.5보다 작거나 같을 때는 (바텀시트의 높이 * slideOffset) 와 1의 값 중 큰 값을 높이로 설정해준다.
     * 절반 보다 클 때는 바텀시트의 절반 높이 만큼을 높이로 설정해준다.*/
    private fun initBottomSheetBehavior() {
        val emptyMemberContainerView = binding.includeBottomLeaderDefaultPlace.bottomLeaderDefaultPlaceContainerEmptyMemeber
        BottomSheetBehavior.from(binding.fgLeaderDefaultPlaceBottomSheet).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> { emptyMemberContainerView.isVisible = true }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> { emptyMemberContainerView.isVisible = false }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    val interactionView = binding.fgLeaderDefaultPlaceViewInteraction
                    interactionView.layoutParams = interactionView.layoutParams.apply {
                        height = if (slideOffset <= 0.5f) {
                            max((bottomSheet.height * slideOffset).toInt(), 1)
                        } else {
                            (bottomSheet.height * 0.5f).toInt()
                        }
                    }
                }
            })
        }
    }
}