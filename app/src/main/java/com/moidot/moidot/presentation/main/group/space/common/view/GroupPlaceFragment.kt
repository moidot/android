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
import com.moidot.moidot.data.data.BestRegionItem
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.databinding.FragmentGroupPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.common.adapter.BestRegionAdapter
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
    private val bestRegionAdapter by lazy { BestRegionAdapter() }

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

    /** dp로 설정하면 가로가 짧은 기기 혹은 긴 기기가 잘리는 현상이 있다.
     * 그래서 기준을 screenWidth의 기준으로 설정해주었다.*/
    private fun setRegionNameVpEffect() {
        val screenWidth = resources.displayMetrics.widthPixels
        val pageMarginPx = (screenWidth * 0.05).toInt()
        val pagerWidth = (screenWidth * 0.7).toInt()
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.fgGroupPlaceVpBestRegionName.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }
        binding.fgGroupPlaceVpBestRegionName.offscreenPageLimit = 2
    }

    private fun setBestRegionNameVpEvent() {
        binding.fgGroupPlaceVpBestRegionName.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bestRegionNameAdapter.updateSelectedPosition(position)
                val selectedMoveUserInfo = viewModel.bestRegions.value?.get(position)!!.moveUserInfo
                bestRegionAdapter.submitList(selectedMoveUserInfo)
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
            initBestRegionNameAdapter(data.map { BestRegionItem(it.name, false) })
            initBestRegionAdapter(data.map { it.moveUserInfo[0] })
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

    private fun initBestRegionNameAdapter(regionsName: List<BestRegionItem>) {
        bestRegionNameAdapter.updateItems(regionsName)
        binding.fgGroupPlaceVpBestRegionName.adapter = bestRegionNameAdapter
    }

    private fun initBestRegionAdapter(regions: List<ResponseBestRegion.Data.MoveUserInfo>) {
        binding.bottomGroupPlaceRvGroupInfo.apply {
            itemAnimator = null
            adapter = bestRegionAdapter
        }
        bestRegionAdapter.submitList(regions)
    }
}