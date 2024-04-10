package com.moidot.moidot.presentation.main.group.space.leader.vote.empty

import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderVoteEmptyBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.util.Constant
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.share.KakaoFeedSetting
import com.moidot.moidot.util.share.KakaoShareManager

class LeaderVoteEmptyFragment: BaseFragment<FragmentLeaderVoteEmptyBinding>(R.layout.fragment_leader_vote_empty) {

    private val groupId by lazy { arguments?.getInt(GROUP_ID) ?: -1 }
    private val groupName by lazy { arguments?.getString(GROUP_NAME) ?: getString(R.string.all_moidot_space)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
    }

    fun shareInvitationWithKakao() {
        val kakaoFeedSetting = KakaoFeedSetting(groupId, groupName)
        KakaoShareManager(requireContext(), kakaoFeedSetting).shareLink()
    }

}