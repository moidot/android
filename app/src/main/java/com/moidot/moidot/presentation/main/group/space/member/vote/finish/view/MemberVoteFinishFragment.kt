package com.moidot.moidot.presentation.main.group.space.member.vote.finish.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.OrderingType
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.databinding.FragmentMemberVoteFinishBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.member.vote.finish.viewmodel.MemberVoteFinishViewModel
import com.moidot.moidot.presentation.main.group.space.member.vote.progress.adapter.VoteProgressInfoAdapter
import com.moidot.moidot.util.Constant
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MemberVoteFinishFragment: BaseFragment<FragmentMemberVoteFinishBinding>(R.layout.fragment_member_vote_finish) {

    private val groupId by lazy { arguments?.getInt(Constant.GROUP_ID) ?: -1 }

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    private val voteProgressInfoAdapter by lazy { VoteProgressInfoAdapter() }
    private val viewModel: MemberVoteFinishViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        loadData()
        setupObservers()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun loadData() {
        viewModel.loadVoteStatus(groupId)
    }

    private fun setupObservers() {
        setupVoteStatuesObserver()
        setupEndDateObserver()
    }

    // 추천 장소 조회뷰
    private fun setupVoteStatuesObserver() {
        viewModel.voteStatuses.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                val centerLatLang = async { // 중심좌표 계산 먼저 완료한 후 지도 초기화
                    MapViewUtil.getMapCenterPointByMapPoints(it.map { LatLng.from(it.latitude, it.longitude) })
                }.await()
                initMapView(it, centerLatLang)
                initStatusesAdapter(it)
            }
        }
    }

    // TODO 종료 날짜 있을 때 분기처리
    private fun setupEndDateObserver() {
        viewModel.endAt.observe(viewLifecycleOwner) {
            binding.fgMemberVoteFinishContainerEndDate.isVisible = it != "none"
        }
    }

    private fun initStatusesAdapter(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>) {
        voteProgressInfoAdapter.apply {
            progressStatuses = voteStatuses
            totalVoteNum = viewModel.totalVoteNum.value!!
            binding.fgMemberVoteFinishRvVoteState.adapter = this
        }
    }

    private fun initMapView(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>, centerLatLang: LatLng) {
        mapManager = MarkerManager(requireContext())
        binding.fgMemberVoteFinishMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng {
                return LatLng.from(centerLatLang.latitude, centerLatLang.longitude)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                addLeaderInfoMarker(voteStatuses)
                MapViewUtil.setZoomLevelByVoteLocationsMapPoints(kakaoMap, voteStatuses)
            }
        })
    }

    private fun addLeaderInfoMarker(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>) {
        labelLayer = kakaoMap.labelManager!!.addLayer(
            LabelLayerOptions.from()
                .setOrderingType(OrderingType.Rank)
        )!!

        val voteCnt = voteStatuses.map { it.votes }.sortedDescending().withIndex().map { it.index + 1 }
        Log.d("kite", voteStatuses.map { it.votes }.sortedDescending().toString())
        Log.d("kite", voteCnt.toString())

        voteStatuses.forEachIndexed { idx, voteStatus ->
            labelLayer.addLabel(
                LabelOptions.from(
                    voteStatus.placeName, LatLng.from(voteStatus.latitude, voteStatus.longitude)
                ).setStyles(
                    LabelStyle.from(mapManager.getVoteResultPlaceMarker(voteCnt[idx], voteStatus.placeName)).setApplyDpScale(false)
                )
            )
        }
    }

    private fun calculateRankers() {

    }

}