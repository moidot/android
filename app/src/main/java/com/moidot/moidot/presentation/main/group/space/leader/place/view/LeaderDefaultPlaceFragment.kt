package com.moidot.moidot.presentation.main.group.space.leader.place.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderDefaultPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max


@AndroidEntryPoint
class LeaderDefaultPlaceFragment : BaseFragment<FragmentLeaderDefaultPlaceBinding>(R.layout.fragment_leader_default_place) {

    private lateinit var kakaoMap: KakaoMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        initView()
    }

    private fun initMapView() {
        binding.fgLeaderDefaultPlaceMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("kite", "지도 종료")
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                Log.d("kite", error.toString())
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                kakaoMap = map
            }
        })

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
        val emptyMemberContainerView = binding.includeBottomLeaderDefaultPlace.bottomLeaderDefaultPlaceContainerEmptyMemeber
        BottomSheetBehavior.from(binding.fgLeaderDefaultPlaceBottomSheet).apply {
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