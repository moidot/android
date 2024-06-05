package com.moidot.moidot.presentation.main.group.space.leader.vote.finish.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.moidot.moidot.databinding.FragmentLeaderVoteFinishBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.common.vote.VoteFinishInfoAdapter
import com.moidot.moidot.presentation.main.group.space.leader.vote.create.CreateVoteActivity
import com.moidot.moidot.presentation.main.group.space.leader.vote.finish.viewmodel.LeaderVoteFinishViewModel
import com.moidot.moidot.util.Constant
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.VOTE_RECREATE_STATE
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import com.moidot.moidot.util.popup.vote.PopupVotePeopleDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeaderVoteFinishFragment : BaseFragment<FragmentLeaderVoteFinishBinding>(R.layout.fragment_leader_vote_finish) {

    private val groupId by lazy { arguments?.getInt(GROUP_ID) ?: -1 }
    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    private val voteFinishInfoAdapter by lazy { VoteFinishInfoAdapter(::onMemberShowClickListener) }
    private val viewModel: LeaderVoteFinishViewModel by viewModels()

    // 투표 초기화
    private val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.let { data ->
            val isCreateVoteSuccess = data.getBooleanExtra(Constant.CRATE_VOTE_SUCCESS_STATE, false)
            if (isCreateVoteSuccess) { // 투표 생성 완료
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        loadData()
        setupObservers()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
    }

    private fun loadData() {
        viewModel.loadVoteStatus(groupId)
    }

    private fun setupObservers() {
        setupVoteStatuesObserver()
        setupEndDateObserver()
        setupVoteMemberObserver()
    }

    // 투표한 사람 조회
    private fun setupVoteMemberObserver() {
        viewModel.votePlaceUsersInfo.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) PopupVotePeopleDialog(
                context = requireContext(),
                leaderName = it.filter { people -> people.isAdmin }.map { people -> people.nickName }[0],
                location = viewModel.userVotePlaceName.value!!,
                people = it.map { people -> people.nickName }
            ).show()
        }
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

    private fun setupEndDateObserver() {
        viewModel.endAt.observe(viewLifecycleOwner) {
            binding.fgLeaderVoteFinishContainerEndDate.isVisible = it != "none"
            binding.fgLeaderVoteFinishTvEndDate.text = it
        }
    }

    private fun initStatusesAdapter(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>) {
        voteFinishInfoAdapter.apply {
            progressStatuses = voteStatuses
            totalVoteNum = viewModel.totalVoteNum.value!!
            binding.fgLeaderVoteFinishRvVoteState.adapter = this
        }
    }

    private fun initMapView(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>, centerLatLang: LatLng) {
        mapManager = MarkerManager(requireContext())
        binding.fgLeaderVoteFinishMapView.start(object : KakaoMapReadyCallback() {
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

        val ranks = calculateRankers(voteStatuses)
        voteStatuses.forEachIndexed { idx, voteStatus ->
            labelLayer.addLabel(
                LabelOptions.from(
                    voteStatus.placeName, LatLng.from(voteStatus.latitude, voteStatus.longitude)
                ).setStyles(
                    LabelStyle.from(mapManager.getVoteResultPlaceMarker(ranks[idx], voteStatus.placeName)).setApplyDpScale(false)
                )
            )
        }
    }

    private fun calculateRankers(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>): List<Int> {
        val votes = voteStatuses.map { it.votes }
        return votes.map { vote -> votes.count { it > vote } + 1 }
    }

    // 투표한 사람 조회
    private fun onMemberShowClickListener(bestPlaceId: Int, bestPlaceName: String) {
        viewModel.getUsersVotePlaceInfo(groupId, bestPlaceId, bestPlaceName)
    }

    fun onClickRestartVoteListener() {
        Intent(requireContext(), CreateVoteActivity::class.java).apply {
            putExtra(GROUP_ID, groupId)
            putExtra(VOTE_RECREATE_STATE, true)
            requestLauncher.launch(this)
        }
    }

}