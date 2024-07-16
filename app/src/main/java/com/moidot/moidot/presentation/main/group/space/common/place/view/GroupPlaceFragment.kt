package com.moidot.moidot.presentation.main.group.space.common.place.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.moidot.moidot.presentation.main.group.space.common.place.adapter.BestRegionNameAdapter
import com.moidot.moidot.presentation.main.group.space.common.place.adapter.MoveUserInfoAdapter
import com.moidot.moidot.presentation.main.group.space.common.place.viewmodel.GroupPlaceViewModel
import com.moidot.moidot.util.Constant.PLACE_NAME_EXTRA
import com.moidot.moidot.util.Constant.PLACE_X_EXTRA
import com.moidot.moidot.util.Constant.PLACE_Y_EXTRA
import com.moidot.moidot.util.DialogUtil
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import com.moidot.moidot.util.SpannableTxt
import com.moidot.moidot.util.view.dpToPx
import com.moidot.moidot.util.view.getScreenHeight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
        setMoveToRecommendPlaceBtnEvent()
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

    private fun setMoveToRecommendPlaceBtnEvent() {
        binding.bottomGroupPlaceBtnDetailSearch.setOnClickListener {
            val currentPos: Int = runBlocking { viewModel.currentPos.firstOrNull() ?: 0 }
            val currentRegion = viewModel.bestRegions.value?.get(currentPos)
            Intent(requireContext(), RecommendPlaceActivity::class.java).apply {
                putExtra(PLACE_NAME_EXTRA, currentRegion?.name)
                putExtra(PLACE_X_EXTRA, currentRegion?.latitude)
                putExtra(PLACE_Y_EXTRA, currentRegion?.longitude)
                startActivity(this)
            }
        }
    }

    private fun setupObservers() {
        setLoadingDialog()
        setupMyInfo()
        setupBestRegionsObserver()
        setupCurPosObserver()
    }

    private fun setLoadingDialog() {
        val dialogUtil = DialogUtil(requireContext())
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) dialogUtil.show() else dialogUtil.dismiss()
        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentPos.collect { position ->
                    val currentRegion = viewModel.bestRegions.value?.get(position)!! // 선택된 추천 지역 정보
                    updateAdapterInfo(position, currentRegion) // rv, vp 정보 갱신
                    if (viewModel.isMapInitialized.value == true) { // 지도 초기화 이후 작업
                        if (kakaoMap.labelManager != null) {
                            kakaoMap.labelManager?.removeAllLabelLayer()
                            routeLine.remove() // 기존 path 삭제
                            routeLineManager.layer.removeAll() // 기존 path 삭제
                        } // 기존 마커 삭제
                        setUpSearchDetailBtnTxt(currentRegion.name)
                        initLabelLayerAndRouteManager()
                        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(currentRegion.latitude, currentRegion.longitude))) // 위치 좌표 설정
                        MapViewUtil.setZoomLevelByUserLocationMapPoints(kakaoMap, currentRegion.moveUserInfo) // 지도 줌 레벨 설정
                        addBestRegionPlaceMarker(currentRegion.name, currentRegion.longitude, currentRegion.latitude)// 추천 장소 마커 추가
                        addUserInfoMarkers(currentRegion.moveUserInfo)// 유저 정보 마커 추가
                        addMovePathRoutineLines(currentRegion.moveUserInfo) // 유저 path 그리기
                    }
                }
            }
        }
    }

    private fun setUpSearchDetailBtnTxt(regionName: String) {
        val content = resources.getString(R.string.space_place_btn_search_detail).format(regionName)
        val spannableTxt = SpannableTxt(requireContext()).applySpannableStyles(
            content = content, target = regionName, styleResId = R.style.b2_bold_14
        )
        binding.bottomGroupPlaceBtnDetailSearch.text = spannableTxt
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
                setUpSearchDetailBtnTxt(bestRegions[0].name)
                MapViewUtil.setZoomLevelByUserLocationMapPoints(kakaoMap, bestRegions[0].moveUserInfo) // 줌 레벨 설정
                addBestRegionPlaceMarker(bestRegions[0].name, bestRegions[0].longitude, bestRegions[0].latitude) // 추천 장소 마커 추가
                addUserInfoMarkers(bestRegions[0].moveUserInfo) // 유저 정보 마커 추가
                addMovePathRoutineLines(bestRegions[0].moveUserInfo) // 루트 정보 추가
                BottomSheetBehavior.from(binding.fgGroupPlaceBottomSheet).state = BottomSheetBehavior.STATE_HALF_EXPANDED // 바텀 시트 초가화
            }
        })
    }

    private fun initLabelLayerAndRouteManager() {
        labelLayer = kakaoMap.labelManager!!.addLayer(
            LabelLayerOptions.from().setOrderingType(OrderingType.Rank)
        )!!
        routeLineManager = kakaoMap.routeLineManager!!
    }

    // 추천 장소 마커 추가
    private fun addBestRegionPlaceMarker(name: String, long: Double, lat: Double) {
        labelLayer.addLabel(
            LabelOptions.from(
                "bestRegion", LatLng.from(lat, long)
            ).setStyles(
                LabelStyle.from(mapManager.getBestRegionPlaceMarker(name)).setApplyDpScale(false).setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale)),
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

    private fun addMyInfoMarker(moveUserInfo: ResponseBestRegion.Data.MoveUserInfo) {
        labelLayer.addLabel(
            LabelOptions.from( // 첫번째 배열이 유저의 시작 위치
                moveUserInfo.userName, LatLng.from(moveUserInfo.path[0].y, moveUserInfo.path[0].x)
            ).setStyles(
                LabelStyle.from(mapManager.getMyPlaceMarker(moveUserInfo.userName)).setApplyDpScale(false).setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale)),
            )
        )
    }

    private fun addOtherInfoMarker(moveUserInfo: ResponseBestRegion.Data.MoveUserInfo) {
        labelLayer.addLabel(
            LabelOptions.from( // 첫번째 배열이 유저의 시작 위치
                moveUserInfo.userName, LatLng.from(moveUserInfo.path[0].y, moveUserInfo.path[0].x)
            ).setStyles(
                LabelStyle.from(mapManager.getOtherPlaceMarker(moveUserInfo.userName)).setApplyDpScale(false).setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale)),
            )
        )
    }

    private fun addMovePathRoutineLines(moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>) {
        val othersMoveInfos = moveUserInfos.filterNot { moveUserInfo -> moveUserInfo.userName == activityViewModel.getUserName() }
        othersMoveInfos.forEach { moveUserInfo ->
            addOtherMovePathRoutineLine(moveUserInfo)
        }
        val myMoveInfo = moveUserInfos.subtract(othersMoveInfos.toSet()).toList()
        addMyMovePathRoutineLine(myMoveInfo[0])
    }

    private fun addMyMovePathRoutineLine(moveUserInfo: ResponseBestRegion.Data.MoveUserInfo) {
        val stylesSet = RouteLineStylesSet.from(
            "myStyles", RouteLineStyles.from(RouteLineStyle.from(5.dpToPx(requireContext()).toFloat(), resources.getColor(R.color.orange500, null)))
        )
        val segment = RouteLineSegment.from(moveUserInfo.path.map { LatLng.from(it.y, it.x) }).setStyles(stylesSet.getStyles(0))
        val options = RouteLineOptions.from(segment).setStylesSet(stylesSet)
        routeLine = routeLineManager.layer.addRouteLine(options)
    }

    private fun addOtherMovePathRoutineLine(moveOtherUserInfo: ResponseBestRegion.Data.MoveUserInfo) {
        val stylesSet = RouteLineStylesSet.from(
            "otherStyles", RouteLineStyles.from(RouteLineStyle.from(3.dpToPx(requireContext()).toFloat(), resources.getColor(R.color.gray500, null)))
        ) // 굵기 단위는 픽셀
        val segment = RouteLineSegment.from(moveOtherUserInfo.path.map { LatLng.from(it.y, it.x) }).setStyles(stylesSet.getStyles(0))
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
        moveUserInfoAdapter.myName = activityViewModel.getUserName()
        moveUserInfoAdapter.submitList(moveUserInfos)
    }
}