package com.moidot.moidot.presentation.main.group.space.leader.vote.progress.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.moidot.moidot.databinding.FragmentLeaderVoteProgressBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.leader.vote.progress.viewmodel.LeaderVoteProgressViewModel
import com.moidot.moidot.presentation.main.group.space.member.vote.progress.adapter.VoteProgressInfoAdapter
import com.moidot.moidot.util.Constant.CRATE_VOTE_MSG_EXTRA
import com.moidot.moidot.util.Constant.CRATE_VOTE_SUCCESS_STATE
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.CustomSnackBar
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeaderVoteProgressFragment : BaseFragment<FragmentLeaderVoteProgressBinding>(R.layout.fragment_leader_vote_progress) {

    private val groupId by lazy { arguments?.getInt(GROUP_ID) ?: -1 }
    private val voteCreateState by lazy { arguments?.getBoolean(CRATE_VOTE_SUCCESS_STATE) ?: false }
    private val voteCreateSnackBarMsg by lazy { arguments?.getString(CRATE_VOTE_MSG_EXTRA) ?: getString(R.string.create_vote_end_time_msg_vote_start) }

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    private val voteProgressInfoAdapter by lazy { VoteProgressInfoAdapter() }
    private val viewModel: LeaderVoteProgressViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSnackBarAfterVoteCreation()
        initBinding()
        loadData()
        setupObservers()
    }

    private fun showSnackBarAfterVoteCreation() {
        if (voteCreateState) CustomSnackBar.makeSnackBar(binding.root, voteCreateSnackBarMsg).show()
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
        setupVoteDoneObserver()
        setupVoteEndObserver()
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
            binding.fgLeaderVoteProgressContainerEndDate.isVisible = it != "none"
            binding.fgLeaderVoteProgressTvEndDate.text = it
        }
    }

    // 투표를 기입한 상태이기 때문에 다시 투표하기 버튼을 누를때까지
    // 체크 박스의 클릭을 비활성화 시킨다.
    // TODO 체크박스 비활성화 하기
    private fun setupVoteDoneObserver() {
        viewModel.isVoteDone.observe(viewLifecycleOwner) {
            binding.fgLeaderVoteProgressBtnVote.text = getString(R.string.leader_vote_progress_btn_re_vote)
        }
    }

    private fun setupVoteEndObserver() {
        viewModel.isVoteEnd.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(LeaderVoteProgressFragmentDirections.actionLeaderVoteProgressFragmentToLeaderVoteFinishFragment())
        }
    }

    private fun initStatusesAdapter(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>) {
        voteProgressInfoAdapter.apply {
            progressStatuses = voteStatuses
            Log.d("kite", progressStatuses.toString())
            totalVoteNum = viewModel.totalVoteNum.value!!
            binding.fgLeaderVoteProgressRvVoteState.adapter = this
        }
    }

    private fun initMapView(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>, centerLatLang: LatLng) {
        mapManager = MarkerManager(requireContext())
        binding.fgLeaderVoteProgressMapView.start(object : KakaoMapReadyCallback() {
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

    fun onVoteClickListener() {
        if (!voteProgressInfoAdapter.getVoteState()) {
            voteProgressInfoAdapter.updateVoteStateTrue()
        } else {
            val bestPlaceIds = voteProgressInfoAdapter.progressStatuses.filter { it.isVoted }.map { it.bestPlaceId }
            viewModel.votePlace(groupId, bestPlaceIds)
        }
        setVoteStateUI(voteProgressInfoAdapter.getVoteState())
    }

    private fun setVoteStateUI(voteState: Boolean) {
        if (voteState) {
            binding.fgLeaderVoteProgressBtnVote.text = getString(R.string.leader_vote_progress_btn_done)
            binding.fgLeaderVoteProgressTvEnd.apply {
                isEnabled = false
                setTextColor(context.getColor(R.color.orange200))
            }
        } else {
            binding.fgLeaderVoteProgressBtnVote.text = getString(R.string.leader_vote_progress_btn_vote)
            binding.fgLeaderVoteProgressTvEnd.apply {
                isEnabled = true
                setTextColor(context.getColor(R.color.orange500))
            }
        }
    }

    fun endVote() {
        viewModel.endVote(groupId)
    }
}