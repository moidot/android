package com.moidot.moidot.presentation.main.group.space.member.vote.progress.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.user.Constants.USER_ID
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.OrderingType
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.databinding.FragmentMemberVoteProgressBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.member.vote.progress.adapter.VoteProgressInfoAdapter
import com.moidot.moidot.presentation.main.group.space.member.vote.progress.viewmodel.MemberVoteProgressViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MemberVoteProgressFragment : BaseFragment<FragmentMemberVoteProgressBinding>(R.layout.fragment_member_vote_progress) {

    private val groupId by lazy { arguments?.getInt(GROUP_ID) ?: -1 }
    private val userId by lazy { arguments?.getInt(USER_ID) ?: -1 }

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    private val voteProgressInfoAdapter by lazy { VoteProgressInfoAdapter(::onMemberShowClickListener) }
    private val viewModel: MemberVoteProgressViewModel by viewModels()

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
        viewModel.loadVoteStatus(groupId, userId)
    }

    private fun setupObservers() {
        setupVoteStatuesObserver()
        setupEndDateObserver()
    }

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

    private fun setupEndDateObserver() {
        viewModel.endAt.observe(viewLifecycleOwner) {
            binding.fgMemberVoteProgressContainerEndDate.isVisible = it != "none"
            binding.fgMemberVoteProgressTvEndDate.text = it
        }
    }

    private fun initStatusesAdapter(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>) {
        voteProgressInfoAdapter.apply {
            progressStatuses = voteStatuses
            totalVoteNum = viewModel.totalVoteNum.value!!
            binding.fgMemberVoteProgressRvVoteState.adapter = this
        }
    }

    private fun initMapView(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>, centerLatLang: LatLng) {
        mapManager = MarkerManager(requireContext())
        binding.fgMemberVoteProgressMapView.start(object : KakaoMapReadyCallback() {
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

        voteStatuses.forEach {
            labelLayer.addLabel(
                LabelOptions.from(
                    it.placeName, LatLng.from(it.latitude, it.longitude)
                ).setStyles(
                    LabelStyle.from(mapManager.getBestRegionPlaceMarker(it.placeName)).setApplyDpScale(false)
                )
            )
        }
    }

    fun startVote() {
        voteProgressInfoAdapter.updateVoteState(true)
    }

    private fun onMemberShowClickListener(bestPlaceId: Int, bestPlaceName: String) {

    }
}