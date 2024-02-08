package com.moidot.moidot.presentation.main.group.space.member

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityMemeberSpaceBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.group.space.SpaceViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberSpaceActivity : BaseActivity<ActivityMemeberSpaceBinding>(R.layout.activity_memeber_space) {

    private val viewModel: SpaceViewModel by viewModels()

    private val groupId by lazy { intent.getIntExtra(GROUP_ID, 0) }
    private val groupName by lazy { intent.getStringExtra(GROUP_NAME) ?: "" }

    private val navController by lazy { findNavController(R.id.member_space_fcv) }

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
    }

    private fun initView() {
        setTabLayout()
    }

    private fun setTabLayout() {
        initTabNavigation()
        initTabSelectedListener()
    }

    private fun initTabNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.member_space_fcv) as NavHostFragment
        navHost.findNavController().setGraph(R.navigation.member_space_nav_graph)
    }

    private fun initTabSelectedListener() {
        binding.memberSpaceTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> navController.navigate(R.id.groupPlaceFragment)
                    1 -> navController.navigate(R.id.memberVoteFragment)
                    2 -> navController.navigate(R.id.memberInfoFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}