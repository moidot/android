package com.moidot.moidot.presentation.main.group.space.common.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentGroupPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.common.adapter.BestRegionNameAdapter
import com.moidot.moidot.presentation.main.group.space.common.viewmodel.GroupPlaceViewModel
import com.moidot.moidot.presentation.main.group.space.leader.LeaderSpaceViewModel
import com.moidot.moidot.util.view.getScreenHeight
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class GroupPlaceFragment : BaseFragment<FragmentGroupPlaceBinding>(R.layout.fragment_group_place) {

    private lateinit var kakaoMap: KakaoMap

    private val viewModel: GroupPlaceViewModel by viewModels()
    private val activityViewModel: LeaderSpaceViewModel by activityViewModels()

    private val bestRegionNameAdapter by lazy { BestRegionNameAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBestRegions(activityViewModel.groupId.value!!)
        initView()
        setupObserver()
    }

    private fun initView() {
        setRegionNameVpEffect()
        setBestRegionNameVpEvent()
        initBottomSheetBehavior()
    }

    private fun setRegionNameVpEffect() {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.view_pager_margin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.view_pager_width)
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.fgGroupPlaceVpBestRegionName.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }
        binding.fgGroupPlaceVpBestRegionName.offscreenPageLimit = 2
    }

    private fun setBestRegionNameVpEvent() {
        binding.fgGroupPlaceVpBestRegionName.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)  // TODO 바텀시트 정보 갱신하기

            }
        })
    }

    private fun initBottomSheetBehavior() {
        val interactionView = binding.fgGroupPlaceViewInteraction
        BottomSheetBehavior.from(binding.fgGroupPlaceBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED // 초기값 고정
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}

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

    private fun setupObserver() {
        viewModel.bestRegions.observe(viewLifecycleOwner) { data ->
            initMapView()
            initBestRegionNameAdapter(data.map { it.name })
        }
    }

    private fun initMapView() {
        binding.fgGroupPlaceMapView.layoutParams.height = getScreenHeight(requireContext())
        binding.fgGroupPlaceMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng { // TODO 모임 중심위치 정보
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

    private fun initBestRegionNameAdapter(regionsName: List<String>) {
        bestRegionNameAdapter.updateItems(regionsName)
        binding.fgGroupPlaceVpBestRegionName.adapter = bestRegionNameAdapter
    }
}