package com.moidot.moidot.presentation.ui.main.group.space.leader.info.view

import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderInfoBinding
import com.moidot.moidot.presentation.ui.base.BaseFragment
import com.moidot.moidot.presentation.util.share.KakaoFeedSetting
import com.moidot.moidot.presentation.util.share.KakaoShareManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderInfoFragment : BaseFragment<FragmentLeaderInfoBinding>(R.layout.fragment_leader_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
    }


    fun shareInvitationWithKakao() {
        val kakaoFeedSetting = KakaoFeedSetting(37, "모이닷 팀 스페이스") // TODO 그룹 정보 반영
        KakaoShareManager(requireContext(), kakaoFeedSetting).shareLink()
    }
}