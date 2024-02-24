package com.moidot.moidot.presentation.main.group.space.leader.vote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderVoteBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.SpaceViewModel
import com.moidot.moidot.util.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderVoteFragment : BaseFragment<FragmentLeaderVoteBinding>(R.layout.fragment_leader_vote) {

    val viewModel: LeaderVoteViewModel by viewModels()
    private val activityViewModel: SpaceViewModel by activityViewModels()
    private val groupId by lazy { activityViewModel.groupId.value!! }
    private val groupParticipates by lazy { activityViewModel.groupParticipates.value!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadVoteStatus(groupId)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.groupVoteStatus.observe(viewLifecycleOwner) {
            when { // TODO 서버 분께 분기처리 변수 확인 요청
                groupParticipates <= 1 -> initNavigation(R.id.leaderVoteEmptyFragment) // 모임원 초대 유도
                !it.isClosed && it.voteStatuses.isNotEmpty() -> initNavigation(R.id.leaderVoteProgressFragment) // 투표 진행중
                it.voteId == -1 && !it.isClosed -> initNavigation(R.id.leaderVoteBeforeFragment) // 투표 시작 전 (최초 투표)
                it.voteId != -1 && !it.isClosed -> initNavigation(R.id.leaderVoteBeforeFragment) // 투표 시작 전 (재투표)
                else -> initNavigation(R.id.leaderVoteFinishFragment) // 투표 종료
            }
        }
    }

    private fun initNavigation(startDestinationId: Int) {
        val extras = Bundle().apply { putInt(Constant.GROUP_ID, groupId) }
        val navController = (childFragmentManager.findFragmentById(R.id.fg_group_leader_vote_fcv) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.leader_vote_nav_graph)
        navGraph.setStartDestination(startDestinationId)
        navController.setGraph(navGraph, extras)
    }
}