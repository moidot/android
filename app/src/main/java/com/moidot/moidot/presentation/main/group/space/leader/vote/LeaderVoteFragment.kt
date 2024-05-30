package com.moidot.moidot.presentation.main.group.space.leader.vote

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.user.Constants.USER_ID
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderVoteBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.SpaceViewModel
import com.moidot.moidot.util.Constant
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.Constant.VOTE_RECREATE_STATE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderVoteFragment : BaseFragment<FragmentLeaderVoteBinding>(R.layout.fragment_leader_vote) {

    val viewModel: LeaderVoteViewModel by viewModels()
    private val activityViewModel: SpaceViewModel by activityViewModels()
    private val groupId by lazy { activityViewModel.groupId.value!! }
    private val groupName by lazy { activityViewModel.groupName.value }
    private val groupParticipates by lazy { activityViewModel.groupParticipates.value!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadVoteStatus(groupId)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.groupVoteStatus.observe(viewLifecycleOwner) {
            when {
                groupParticipates <= 1 -> initNavigation(R.id.leaderVoteEmptyFragment) // 모임원 초대 유도
                !it.isClosed && it.voteStatuses.isNotEmpty() -> initNavigation(R.id.leaderVoteProgressFragment) // 투표 진행중
                it.voteId == -1 && !it.isClosed -> initNavigation(R.id.leaderVoteBeforeFragment, VOTE_CREATE) // 투표 시작 전 (최초 투표)
                it.voteId != -1 && !it.isClosed -> initNavigation(R.id.leaderVoteBeforeFragment, VOTE_RECREATE) // 투표 시작 전 (재투표)
                else -> initNavigation(R.id.leaderVoteFinishFragment) // 투표 종료
            }
        }
    }

    private fun initNavigation(startDestinationId: Int, reCreateState: Boolean = false) {
        val extras = Bundle().apply {
            putInt(GROUP_ID, groupId)
            putString(GROUP_NAME, groupName)
            putBoolean(VOTE_RECREATE_STATE, reCreateState) // 재투표 분기 처리
        }
        val navController = (childFragmentManager.findFragmentById(R.id.fg_group_leader_vote_fcv) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.leader_vote_nav_graph)
        navGraph.setStartDestination(startDestinationId)
        navController.setGraph(navGraph, extras)
    }

    companion object {
        const val VOTE_CREATE = false
        const val VOTE_RECREATE = true
    }
}