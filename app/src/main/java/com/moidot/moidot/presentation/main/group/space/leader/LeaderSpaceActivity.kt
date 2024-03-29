package com.moidot.moidot.presentation.main.group.space.leader

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityLeaderSpaceBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.group.space.SpaceViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_MEMBER_COUNT
import com.moidot.moidot.util.Constant.GROUP_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderSpaceActivity : BaseActivity<ActivityLeaderSpaceBinding>(R.layout.activity_leader_space) {

    private val viewModel: SpaceViewModel by viewModels()

    private val groupId by lazy { intent.getIntExtra(GROUP_ID, 0) }
    private val groupParticipates by lazy { intent.getIntExtra(GROUP_MEMBER_COUNT, 1) }
    private val groupName by lazy { intent.getStringExtra(GROUP_NAME) ?: "" }

    private val navController by lazy { findNavController(R.id.leader_space_fcv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setPrevData()
        initView()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun setPrevData() {
        viewModel.setGroupId(groupId)
        viewModel.setGroupName(groupName)
        viewModel.setGroupParticipates(groupParticipates)
    }

    private fun initView() {
        setTabLayout()
    }

    private fun setTabLayout() {
        initTabNavigation()
        initTabSelectedListener()
    }

    // 참여 인원에 따른 초기화 화면 분기 처리
    private fun initTabNavigation() {
        val navController = (supportFragmentManager.findFragmentById(R.id.leader_space_fcv) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.leader_space_nav_graph)
        val startDestinationId = if (groupParticipates > 1) R.id.groupPlaceFragment else R.id.leaderPlaceFragment
        navGraph.setStartDestination(startDestinationId)
        navController.setGraph(navGraph, null)
    }

    private fun initTabSelectedListener() {
        binding.leaderSpaceTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> if (viewModel.groupParticipates.value!! > 1) navController.navigate(R.id.groupPlaceFragment) else navController.navigate(R.id.leaderPlaceFragment)
                    1 -> navController.navigate(R.id.leaderVoteFragment)
                    2 -> navController.navigate(R.id.leaderInfoFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}