package com.moidot.moidot.presentation.ui.main.group.space.member

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityMemeberSpaceBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity

class MemberSpaceActivity : BaseActivity<ActivityMemeberSpaceBinding>(R.layout.activity_memeber_space) {

    private val navController by lazy { findNavController(R.id.member_space_fcv) }

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
        val navHost = supportFragmentManager.findFragmentById(R.id.member_space_fcv) as NavHostFragment
        navHost.findNavController().setGraph(R.navigation.member_space_nav_graph)
    }

    private fun initTabSelectedListener() {
        val navController = findNavController(R.id.member_space_fcv)
        binding.memberSpaceTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> navController.navigate(R.id.memberPlaceFragment)
                    1 -> navController.navigate(R.id.memberVoteFragment)
                    2 -> navController.navigate(R.id.memberInfoFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}