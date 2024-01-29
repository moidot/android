package com.moidot.moidot.presentation.ui.main.group.space.leader

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityLeaderSpaceBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.util.Constant.GROUP_LEADER_NAME
import com.moidot.moidot.presentation.util.Constant.GROUP_ID
import com.moidot.moidot.presentation.util.Constant.GROUP_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderSpaceActivity : BaseActivity<ActivityLeaderSpaceBinding>(R.layout.activity_leader_space) {

    val groupId by lazy { intent.getIntExtra(GROUP_ID, 0) }
    val groupName by lazy { intent.getStringExtra(GROUP_NAME) }
    val groupLeaderName by lazy { intent.getStringExtra(GROUP_LEADER_NAME) }

    private val navController by lazy { findNavController(R.id.leader_space_fcv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initView() {
        setTabLayout()
    }

    private fun setTabLayout() {
        initTabNavigation()
        initTabSelectedListener()
    }

    private fun initTabNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.leader_space_fcv) as NavHostFragment
        navHost.findNavController().setGraph(R.navigation.leader_space_nav_graph)
    }

    private fun initTabSelectedListener() {
        binding.leaderSpaceTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> navController.navigate(R.id.leaderPlaceFragment)
                    1 -> navController.navigate(R.id.leaderVoteFragment)
                    2 -> navController.navigate(R.id.leaderInfoFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}