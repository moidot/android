package com.moidot.moidot.presentation.main.group.space.leader.place.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.util.MarkerManager
import com.moidot.moidot.util.view.getScreenHeight
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class LeaderPlaceFragment : BaseFragment<FragmentLeaderPlaceBinding>(R.layout.fragment_leader_place) {

    private lateinit var kakaoMap: KakaoMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        initView()
    }

    private fun initMapView() {
        binding.fgLeaderPlaceMapView.layoutParams.height = getScreenHeight(requireContext())
        binding.fgLeaderPlaceMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng { // TODO 모임장의 위치 정보
                return LatLng.from(37.4005, 127.1101)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                disableGestures()
            }
        })
    }

    private fun disableGestures() {
        GestureType.values().forEach { kakaoMap.setGestureEnable(it, false) }
    }

    private fun initView() {
        initBottomSheetBehavior()
    }

    private fun initBottomSheetBehavior() {
        val interactionView = binding.fgLeaderPlaceViewInteraction
        val emptyMemberContainerView = binding.includeBottomLeaderDefaultPlace.bottomLeaderDefaultPlaceContainerEmptyMemeber
        BottomSheetBehavior.from(binding.fgLeaderPlaceBottomSheet).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            emptyMemberContainerView.isVisible = true
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            emptyMemberContainerView.isVisible = false
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
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