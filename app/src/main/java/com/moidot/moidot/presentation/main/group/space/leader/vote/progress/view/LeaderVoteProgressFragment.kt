package com.moidot.moidot.presentation.main.group.space.leader.vote.progress.view

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.moidot.moidot.databinding.FragmentLeaderVoteProgressBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.leader.vote.progress.viewmodel.LeaderVoteProgressViewModel
import com.moidot.moidot.presentation.main.group.space.common.vote.VoteProgressInfoAdapter
import com.moidot.moidot.util.Constant.CRATE_VOTE_MSG_EXTRA
import com.moidot.moidot.util.Constant.CRATE_VOTE_SUCCESS_STATE
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.CustomSnackBar
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import com.moidot.moidot.util.popup.vote.PopupVotePeopleDialog
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

    private val voteProgressInfoAdapter by lazy { VoteProgressInfoAdapter(::onMemberShowClickListener) }
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
        setupVoteEndObserver()
        setupVoteMultipleStatusObserver()
        setupVoteAnonymousStatusObserver()
        setupVoteMemberObserver()
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

    // 복수투표 설정
    private fun setupVoteMultipleStatusObserver() {
        viewModel.isAnonymous.observe(viewLifecycleOwner) {
            voteProgressInfoAdapter.isAnonymous = it
        }
    }

    // 익명 투표 설정
    private fun setupVoteAnonymousStatusObserver() {
        viewModel.isEnabledMultipleChoice.observe(viewLifecycleOwner) {
            voteProgressInfoAdapter.isEnabledMultipleChoice = it
        }
    }

    private fun setupVoteEndObserver() {
        viewModel.isVoteEnd.observe(viewLifecycleOwner) {
            if (it) {
                val action = LeaderVoteProgressFragmentDirections.actionLeaderVoteProgressFragmentToLeaderVoteFinishFragment(groupId)
                findNavController().navigate(action)
            }
        }
    }

    // 투표한 사람 조회
    private fun setupVoteMemberObserver() {
        viewModel.votePlaceUsersInfo.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) PopupVotePeopleDialog(
                context = requireContext(),
                leaderName = it.filter { people -> people.isAdmin }.map { people -> people.nickName }[0], // TODO 모임장이 투표 안했을때를 고려
                location = viewModel.userVotePlaceName.value!!,
                people = it.map { people -> people.nickName }
            ).show()
        }
    }

    private fun initStatusesAdapter(voteStatuses: List<ResponseVoteStatus.Data.VoteStatuses>) {
        val isAlreadyVoted = voteStatuses.any { it.isVoted } // 이미 투표한 유저인지 여부 체크
        voteProgressInfoAdapter.apply {
            progressStatuses = voteStatuses
            voteState = isAlreadyVoted
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
        val voteStatus = binding.fgLeaderVoteProgressBtnVote.text
        when (voteStatus) {
            getString(R.string.leader_vote_progress_btn_vote) -> { // 투표하기 -> 투표 완료하기
                binding.fgLeaderVoteProgressBtnVote.text = getString(R.string.leader_vote_progress_btn_done)
                voteProgressInfoAdapter.updateVoteState(true)
                voteProgressInfoAdapter.updateCheckBoxEnableState(true)
                setVoteEndTextStateUI(true)
            }

            getString(R.string.leader_vote_progress_btn_done) -> { // 투표 완료하기 -> 다시 투표하기
                val bestPlaceIds = voteProgressInfoAdapter.progressStatuses.filter { it.isVoted }.map { it.bestPlaceId }

                viewModel.votePlace(groupId, bestPlaceIds)
                voteProgressInfoAdapter.updateCheckBoxEnableState(false)
                binding.fgLeaderVoteProgressBtnVote.text = getString(R.string.leader_vote_progress_btn_re_vote)
                setVoteEndTextStateUI(false)
            }

            getString(R.string.leader_vote_progress_btn_re_vote) -> { // 다시 투표하기 -> 투표 완료하기
                voteProgressInfoAdapter.updateVoteState(true)
                voteProgressInfoAdapter.updateCheckBoxEnableState(true)
                binding.fgLeaderVoteProgressBtnVote.text = getString(R.string.leader_vote_progress_btn_done)
                setVoteEndTextStateUI(true)
            }
        }
    }

    private fun setVoteEndTextStateUI(voteState: Boolean) {
        if (voteState) {
            binding.fgLeaderVoteProgressTvEnd.apply {
                isEnabled = false
                setTextColor(context.getColor(R.color.orange200))
            }
        } else {
            binding.fgLeaderVoteProgressTvEnd.apply {
                isEnabled = true
                setTextColor(context.getColor(R.color.orange500))
            }
        }
    }

    fun endVote() {
        viewModel.endVote(groupId)
    }

    private fun onMemberShowClickListener(bestPlaceId: Int, bestPlaceName:String) {
        viewModel.getUsersVotePlaceInfo(groupId, bestPlaceId, bestPlaceName)
    }
}