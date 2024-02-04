package com.moidot.moidot.presentation.main.group.space.leader.place.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.OrderingType
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderDefaultPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max


@AndroidEntryPoint
class LeaderDefaultPlaceFragment : BaseFragment<FragmentLeaderDefaultPlaceBinding>(R.layout.fragment_leader_default_place) {

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        initView()
    }

    private fun initMapView() {
        // binding.fgLeaderDefaultPlaceMapView.layoutParams.height = getScreenHeight(requireContext())
        mapManager = MarkerManager(requireContext())
        binding.fgLeaderDefaultPlaceMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng { // TODO 모임장의 위치 정보
                 return LatLng.from(37.4005, 127.1101)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                disableGestures()
                addLeaderInfoMarker()
            }
        })
    }

    // 지도 터치 막기
    private fun disableGestures() {
        GestureType.values().forEach { kakaoMap.setGestureEnable(it, false) }
    }

    // 마커 추가
    private fun addLeaderInfoMarker() {
        labelLayer = kakaoMap.labelManager!!.addLayer(
            LabelLayerOptions.from()
                .setOrderingType(OrderingType.Rank)
        )!!

        labelLayer.addLabel(
            LabelOptions.from( // TODO 모임장의 출발 위치
                "default", LatLng.from(37.4005, 127.1101)
            ).setStyles( // TODO 리더의 이름 정보
                LabelStyle.from(mapManager.getDefaultPlaceMarker("정")).setApplyDpScale(false)
            )
        )
    }

    private fun initView() {
        initBottomSheetBehavior()
    }

    /** slideOffset: 0은 완전히 닫힌 상태 1은 완전 펼쳐진 상태이다. slideOffset가
     * 절반 높이인 0.5보다 작거나 같을 때는 (바텀시트의 높이 * slideOffset) 와 1의 값 중 큰 값을 높이로 설정해준다.
     * 절반 보다 클 때는 바텀시트의 절반 높이 만큼을 높이로 설정해준다.
     *
     * 중간 높이 만큼 펼쳐진 상태를 얻기 위해선 behavior_fitToContents 를 사용한다.
     * 이때, 중간 높이는 퍼센트로 지정이 가능하다.
     * 따라서, STATE_HALF_EXPANDED 가 중간 높이에 도달했을 때 컨테이너 뷰를 펼쳐주는 조건을 넣어주었다.
     * */
    private fun initBottomSheetBehavior() {
        val interactionView = binding.fgLeaderDefaultPlaceViewInteraction
        // val gradientView = binding.fgLeaderDefaultPlaceGradientView

        val emptyMemberContainerView = binding.includeBottomLeaderDefaultPlace.bottomLeaderDefaultPlaceContainerEmptyMemeber
        BottomSheetBehavior.from(binding.fgLeaderDefaultPlaceBottomSheet).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            emptyMemberContainerView.isVisible = true
                            kakaoMap.setViewport(binding.fgLeaderDefaultPlaceMapView.width, binding.fgLeaderDefaultPlaceMapView.height)
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            emptyMemberContainerView.isVisible = false
                            kakaoMap.setViewport(binding.fgLeaderDefaultPlaceMapView.width, binding.fgLeaderDefaultPlaceMapView.height)
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