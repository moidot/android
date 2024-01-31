package com.moidot.moidot.ui.main.group.space.member.info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentMemberInfoBinding
import com.moidot.moidot.ui.base.BaseFragment
import com.moidot.moidot.ui.main.group.space.member.MemberSpaceActivity
import com.moidot.moidot.ui.main.group.space.member.info.adapter.MemberGroupInfoHeaderAdapter
import com.moidot.moidot.util.VerticalSpaceItemDecoration
import com.moidot.moidot.util.dpToPx
import com.moidot.moidot.util.popup.PopupTwoButtonDialog
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
        setupGroupDeleteObserver()
        setupToastEventObserver()
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

    private fun setupToastEventObserver() {
        viewModel.showToastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupGroupDeleteObserver() {
        viewModel.isGroupDeleteSuccess.observe(viewLifecycleOwner) {
            if (it) requireActivity().finish()
        }
    }

    // 모임 초대 (카톡)
    fun shareInvitationWithKakao() {
        val kakaoFeedSetting = KakaoFeedSetting(groupId, viewModel.groupName.value!!)
        KakaoShareManager(requireContext(), kakaoFeedSetting).shareLink()
    }

    // 모임 나가기
    fun showGroupDeleteDialog() {
        PopupTwoButtonDialog(requireContext(),
            getString(R.string.space_member_info_dialog_title).format(viewModel.groupName.value),
            getString(R.string.space_member_info_dialog_content),
            getString(R.string.space_member_info_dialog_btn)
        ) { viewModel.deleteGroup(participateId = 83) }.show() // TODO 자신의 정보 받아오기
    }
}