package com.moidot.moidot.ui.main.group.space.member.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentMemberInfoBinding
import com.moidot.moidot.ui.base.BaseFragment
import com.moidot.moidot.ui.main.group.space.member.MemberSpaceActivity
import com.moidot.moidot.ui.main.group.space.member.info.adapter.MemberGroupInfoHeaderAdapter
import com.moidot.moidot.util.VerticalSpaceItemDecoration
import com.moidot.moidot.util.dpToPx
import com.moidot.moidot.util.share.KakaoFeedSetting
import com.moidot.moidot.util.share.KakaoShareManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberInfoFragment : BaseFragment<FragmentMemberInfoBinding>(R.layout.fragment_member_info) {

    private val groupId by lazy { (activity as MemberSpaceActivity).groupId }
    private val memberGroupInfoHeaderAdapter by lazy { MemberGroupInfoHeaderAdapter() }
    private val viewModel: MemberInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
        setupObservers()
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initView() {
        initGroupAdapter()
    }

    private fun initGroupAdapter() {
        binding.fgMemberInfoRvGroupInfo.apply {
            adapter = memberGroupInfoHeaderAdapter
            itemAnimator = null
            addItemDecoration(VerticalSpaceItemDecoration(24.dpToPx(this.context)))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGroupInfo(groupId)
    }

    private fun setupObservers() {
        setupGroupDefaultInfoView()
        setupGroupInfoRecyclerview()
    }

    private fun setupGroupDefaultInfoView() {
        viewModel.groupName.observe(viewLifecycleOwner) {
            binding.fgMemberInfoTvGroupName.text = it
        }
    }

    private fun setupGroupInfoRecyclerview() {
        viewModel.participantsByRegion.observe(viewLifecycleOwner) {
            memberGroupInfoHeaderAdapter.updateItems(it)
        }
    }

    fun shareInvitationWithKakao() {
        val kakaoFeedSetting = KakaoFeedSetting(groupId, viewModel.groupName.value!!)
        KakaoShareManager(requireContext(), kakaoFeedSetting).shareLink()
    }
}