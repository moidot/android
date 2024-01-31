package com.moidot.moidot.presentation.main.group.space.leader.info.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderInfoBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.leader.LeaderSpaceActivity
import com.moidot.moidot.presentation.main.group.space.leader.info.edit.view.EditGroupNameActivity
import com.moidot.moidot.presentation.main.group.space.leader.info.main.adapter.LeaderGroupInfoHeaderAdapter
import com.moidot.moidot.presentation.main.group.space.leader.info.main.viewmodel.LeaderInfoViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.popup.PopupTwoButtonDialog
import com.moidot.moidot.util.share.KakaoFeedSetting
import com.moidot.moidot.util.share.KakaoShareManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderInfoFragment : BaseFragment<FragmentLeaderInfoBinding>(R.layout.fragment_leader_info) {

    private val groupId by lazy { (activity as LeaderSpaceActivity).groupId }
    private val leaderGroupInfoHeaderAdapter by lazy { LeaderGroupInfoHeaderAdapter(::onMemberRemoveListener) }
    private val viewModel: LeaderInfoViewModel by viewModels()

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
        binding.fgLeaderInfoRvGroupInfo.apply {
            adapter = leaderGroupInfoHeaderAdapter
            itemAnimator = null
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
            binding.fgLeaderInfoTvGroupName.text = it
        }
    }

    private fun setupGroupInfoRecyclerview() {
        viewModel.participantsByRegion.observe(viewLifecycleOwner) {
            leaderGroupInfoHeaderAdapter.updateItems(it)
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

    // 모임원 내보내기 뷰 활성화
    fun activateMemberRemovalView() {
        binding.fgLeaderInfoContainerMemberRemoveExit.isVisible = true
        leaderGroupInfoHeaderAdapter.setRemoveMode(ACTIVATE_FLAG)
        setViewTransparencyAndDisable(ACTIVATE_FLAG)
    }

    // 모임원 내보내기 뷰 비활성화
    fun inActiveMemberRemovalView() {
        binding.fgLeaderInfoContainerMemberRemoveExit.isVisible = false
        leaderGroupInfoHeaderAdapter.setRemoveMode(INACTIVATE_FLAG)
        setViewTransparencyAndDisable(INACTIVATE_FLAG)
    }

    /** 활성화 상태일 때 rv와 내보내기 버튼을 제외하고 모두 '투명도'와 disable이 적용된다.
     * 활성화 -> 투명도: rootView 25%, 나머지 0%, 클릭: 루트 x, 나머지 o
     * 원상 복구 -> 투명도: 모두 0% , 클릭: 모두 가능 */
    private fun setViewTransparencyAndDisable(activateFlag: Boolean) {
        val defaultViews = mutableListOf<View>(
            binding.fgLeaderInfoContainerInfo,
            binding.fgLeaderInfoContainerMemberRemove,
            binding.fgLeaderInfoContainerInfoEditMy
        )
        val removalViews = mutableListOf<View>(
            binding.fgLeaderInfoRvGroupInfo,
            binding.fgLeaderInfoContainerMemberRemoveExit
        )
        if (activateFlag) {
            defaultViews.forEach {
                it.alpha = 0.25f
                it.isEnabled = false
            }
            removalViews.forEach {
                it.alpha = 1.0f
            }
        } else {
            defaultViews.forEach {
                it.alpha = 1.0f
                it.isEnabled = true
            }
        }
    }

    private fun onMemberRemoveListener(participateId: Int) {
        viewModel.removeMember(groupId, participateId)
    }

    // 모임 이름 수정
    fun editGroupName() {
        Intent(requireContext(), EditGroupNameActivity::class.java).apply {
            putExtra(GROUP_ID, groupId)
            putExtra(GROUP_NAME, viewModel.groupName.value)
            startActivity(this)
        }
    }

    // 모임 초대
    fun shareInvitationWithKakao() {
        val kakaoFeedSetting = KakaoFeedSetting(groupId, viewModel.groupName.value!!)
        KakaoShareManager(requireContext(), kakaoFeedSetting).shareLink()
    }

    // 모임 삭제
    fun showGroupDeleteDialog() { // TODO 모임 삭제 멘트 수정 예정
        PopupTwoButtonDialog(
            requireContext(),
            getString(R.string.space_member_info_dialog_title).format(viewModel.groupName.value),
            getString(R.string.space_member_info_dialog_content),
            getString(R.string.space_member_info_dialog_btn)
        ) { viewModel.deleteGroup(groupId) }.show() // TODO 자신의 정보 받아오기
    }

    companion object {
        const val ACTIVATE_FLAG = true
        const val INACTIVATE_FLAG = false
    }
}