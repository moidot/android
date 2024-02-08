package com.moidot.moidot.presentation.main.group.space.common.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelTransition
import com.kakao.vectormap.label.OrderingType
import com.kakao.vectormap.label.Transition
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineManager
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.moidot.moidot.R
import com.moidot.moidot.data.data.BestRegionItem
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.databinding.FragmentGroupPlaceBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.SpaceViewModel
import com.moidot.moidot.presentation.main.group.space.common.adapter.BestRegionNameAdapter
import com.moidot.moidot.presentation.main.group.space.common.adapter.MoveUserInfoAdapter
import com.moidot.moidot.presentation.main.group.space.common.viewmodel.GroupPlaceViewModel
import com.moidot.moidot.util.MarkerManager
import com.moidot.moidot.util.view.getScreenHeight
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max


@AndroidEntryPoint
class GroupPlaceFragment : BaseFragment<FragmentGroupPlaceBinding>(R.layout.fragment_group_place) {

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager
    private lateinit var routeLine: RouteLine
    private lateinit var routeLineManager: RouteLineManager

    private val viewModel: GroupPlaceViewModel by viewModels()
    private val activityViewModel: SpaceViewModel by activityViewModels()
    private val bestRegionNameAdapter by lazy { BestRegionNameAdapter() }
    private val moveUserInfoAdapter by lazy { MoveUserInfoAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initView()
        setupObservers()
    }

    private fun loadData() {
        activityViewModel.loadUserInfo()
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
                viewModel.setCurrentPos(position)
            }
        })
    }

    private fun updateAdapterInfo(position: Int, currentRegion: ResponseBestRegion.Data) {
        bestRegionNameAdapter.updateSelectedPosition(position)
        val selectedMoveUserInfo = currentRegion.moveUserInfo
        moveUserInfoAdapter.submitList(selectedMoveUserInfo)
    }

    private fun initBottomSheetBehavior() {
        val interactionView = binding.fgGroupPlaceViewInteraction
        BottomSheetBehavior.from(binding.fgGroupPlaceBottomSheet).apply {
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

    private fun setupObservers() {
        setupMyInfo()
        setupBestRegionsObserver()
        setupCurPosObserver()
    }

    // 유저 정보를 먼저 받아온 뒤에 해당 정보를 바탕으로 장소 정보 세팅해주기
    // path, 띠지, 마커
    private fun setupMyInfo() {
        activityViewModel.userInfo.observe(viewLifecycleOwner) {
            viewModel.getBestRegions(activityViewModel.groupId.value!!)
        }
    }

    private fun setupBestRegionsObserver() {
        viewModel.bestRegions.observe(viewLifecycleOwner) { data ->
            initMapView(data)
            initBestRegionNameAdapter(data.map { BestRegionItem(it.name, false) })
            initBestRegionAdapter(data.map { it.moveUserInfo[0] })
        }
    }

    private fun setupCurPosObserver() {
        viewModel.currentPos.observe(viewLifecycleOwner) { position ->
            val currentRegion = viewModel.bestRegions.value?.get(position)!! // 선택된 추천 지역 정보
            updateAdapterInfo(position, currentRegion) // rv, vp 정보 갱신
            if (viewModel.isMapInitialized.value == true) { // 지도 초기화 이후 작업
                kakaoMap.labelManager!!.removeAllLabelLayer() // 기존 마커 삭제
                routeLine.remove() // 기존 path 삭제
                initLabelLayerAndRouteManager()
                kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(currentRegion.latitude, currentRegion.longitude))) // 위치 좌표 설정
                kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(setZoomLevelByCheckMapPoints(currentRegion.moveUserInfo)))
                addBestRegionPlaceMarker(currentRegion.name, currentRegion.longitude, currentRegion.latitude)// 추천 장소 마커 추가
                addUserInfoMarkers(currentRegion.moveUserInfo)// 유저 정보 마커 추가
                addMovePathRoutineLines(currentRegion.moveUserInfo) // 유저 path 그리기
            }
        }
    }

    private fun initMapView(bestRegions: List<ResponseBestRegion.Data>) {
        binding.fgGroupPlaceMapView.layoutParams.height = getScreenHeight(requireContext())
        mapManager = MarkerManager(requireContext())
        binding.fgGroupPlaceMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng { // 첫번째 지점 위치 받아오기
                return LatLng.from(bestRegions[0].latitude, bestRegions[0].longitude)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                viewModel.isMapInitialized.value = true // 맵 초기화 정보 설정
                initLabelLayerAndRouteManager()
                kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(setZoomLevelByCheckMapPoints(bestRegions[0].moveUserInfo)))
                addBestRegionPlaceMarker(bestRegions[0].name, bestRegions[0].longitude, bestRegions[0].latitude) // 추천 장소 마커 추가
                addUserInfoMarkers(bestRegions[0].moveUserInfo) // 유저 정보 마커 추가
                addMovePathRoutineLines(bestRegions[0].moveUserInfo) // 루트 정보 추가
                BottomSheetBehavior.from(binding.fgGroupPlaceBottomSheet).state = BottomSheetBehavior.STATE_HALF_EXPANDED // 바텀 시트 초가화
            }
        })
    }

    // canShowMapPoints를 사용하여 특정 zoom level에서 해당 좌표들이 다 보이는 지 확인한 후 zoomLevel을 계산하여 반환한다.
    private fun setZoomLevelByCheckMapPoints(moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>): Int {
        val mapPoints = moveUserInfos.map { LatLng.from(it.path[0].y, it.path[0].x) }
        for (level in kakaoMap.maxZoomLevel downTo kakaoMap.minZoomLevel) {
            if (kakaoMap.canShowMapPoints(level, *mapPoints.toTypedArray())) {
                return level.minus(1)
            }
        }
        return kakaoMap.zoomLevel
    }

    private fun initLabelLayerAndRouteManager() {
        labelLayer = kakaoMap.labelManager!!.addLayer(
            LabelLayerOptions.from()
                .setOrderingType(OrderingType.Rank)
        )!!
        routeLineManager = kakaoMap.routeLineManager!!
    }

    // 추천 장소 마커 추가
    private fun addBestRegionPlaceMarker(name: String, long: Double, lat: Double) {
        labelLayer.addLabel(
            LabelOptions.from(
                "bestRegion", LatLng.from(lat, long)
            ).setStyles(
                LabelStyle.from(mapManager.getBestRegionPlaceMarker(name))
                    .setApplyDpScale(false)
                    .setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale)),
            )
        )
    }

    // 유저 위치 정보 마커 추가
    private fun addUserInfoMarkers(moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>) {
        for (i in moveUserInfos.indices) {
            val moveUserInfo = moveUserInfos[i]
            if (moveUserInfo.userName == activityViewModel.getUserName()) { // 본인 마커 추가
                addMyInfoMarker(moveUserInfo)
            } else { // 다른 사람 마커 추가
                addOtherInfoMarker(moveUserInfo)
            }
        }
    }

    private fun addMyInfoMarker(moveUserInfo:ResponseBestRegion.Data.MoveUserInfo) {
        labelLayer.addLabel(
            LabelOptions.from( // 첫번째 배열이 유저의 시작 위치
                moveUserInfo.userName, LatLng.from(moveUserInfo.path[0].y, moveUserInfo.path[0].x)
            ).setStyles(
                LabelStyle.from(mapManager.getMyPlaceMarker(moveUserInfo.userName))
                    .setApplyDpScale(false)
                    .setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale)),
            )
        )
    }

    private fun addOtherInfoMarker(moveUserInfo:ResponseBestRegion.Data.MoveUserInfo) {
        labelLayer.addLabel(
            LabelOptions.from( // 첫번째 배열이 유저의 시작 위치
                moveUserInfo.userName, LatLng.from(moveUserInfo.path[0].y, moveUserInfo.path[0].x)
            ).setStyles(
                LabelStyle.from(mapManager.getOtherPlaceMarker(moveUserInfo.userName))
                    .setApplyDpScale(false)
                    .setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale)),
            )
        )
    }

    // TODO 본인게 아닌건 분기처리
    private fun addMovePathRoutineLines(moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>) {
        val stylesSet = RouteLineStylesSet.from(
            "orangeStyles",
            RouteLineStyles.from(RouteLineStyle.from(13f, resources.getColor(R.color.orange500, null)))
        )
        val segment = RouteLineSegment.from(moveUserInfos.flatMap { it.path }.map { LatLng.from(it.y, it.x) }).setStyles(stylesSet.getStyles(0))
        val options = RouteLineOptions.from(segment).setStylesSet(stylesSet)
        routeLine = routeLineManager.layer.addRouteLine(options)
    }

    private fun initBestRegionNameAdapter(regionsName: List<BestRegionItem>) {
        bestRegionNameAdapter.updateItems(regionsName)
        binding.fgGroupPlaceVpBestRegionName.adapter = bestRegionNameAdapter
    }


    private fun initBestRegionAdapter(moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>) {
        binding.bottomGroupPlaceRvGroupInfo.apply {
            itemAnimator = null
            adapter = moveUserInfoAdapter
        }
        moveUserInfoAdapter.submitList(moveUserInfos)
    }
}