package com.moidot.moidot.presentation.main.group.space.member.vote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentMemberVoteBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.SpaceViewModel
import com.moidot.moidot.util.Constant.MEMBER_VOTE_EXTRA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberVoteFragment : BaseFragment<FragmentMemberVoteBinding>(R.layout.fragment_member_vote) {

    val viewModel: MemberVoteViewModel by viewModels()
    private val activityViewModel: SpaceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadVoteStatus(activityViewModel.groupId.value!!)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.groupVoteStatus.observe(viewLifecycleOwner) {
            when {
                it.voteId == -1 -> initNavigation(R.id.memberVoteBeforeFragment) // 투표 시작 전
                it.isClosed -> initNavigation(R.id.memberVoteFinishFragment) // 투표 종료
                else -> initNavigation(R.id.memberVoteProgressFragment) // 투표 진행 중
            }
        }
    }

    private fun initNavigation(startDestinationId: Int) {
        val extras = Bundle().apply { putParcelable(MEMBER_VOTE_EXTRA, viewModel.groupVoteStatus.value!!) }
        val navController = (childFragmentManager.findFragmentById(R.id.fg_group_member_vote_fcv) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.member_vote_nav_graph)
        navGraph.setStartDestination(startDestinationId)
        navController.setGraph(navGraph, extras)
    }

}