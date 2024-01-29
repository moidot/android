package com.moidot.moidot.presentation.ui.main.group.space.leader.info.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderInfoBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.ui.main.group.space.leader.info.viewmodel.LeaderInfoViewModel
import com.moidot.moidot.presentation.util.share.KakaoFeedSetting
import com.moidot.moidot.presentation.util.share.KakaoShareManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderInfoFragment : BaseFragment<FragmentLeaderInfoBinding>(R.layout.fragment_leader_info) {

    private val viewModel: LeaderInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        setupObservers()
    }

    private fun initBinding() {
        binding.fragment = this
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGroupInfo(37)
    }

    private fun setupObservers() {
        setupGroupInfoRecyclerview()
    }

    private fun setupGroupInfoRecyclerview() {
        viewModel.participantsByRegion.observe(viewLifecycleOwner) {

        }
    }


    fun shareInvitationWithKakao() {
        val kakaoFeedSetting = KakaoFeedSetting(37, "모이닷 팀 스페이스") // TODO 그룹 정보 반영
        KakaoShareManager(requireContext(), kakaoFeedSetting).shareLink()
    }
}