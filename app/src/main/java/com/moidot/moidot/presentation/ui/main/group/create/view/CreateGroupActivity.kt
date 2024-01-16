package com.moidot.moidot.presentation.ui.main.group.create.view

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityCreateGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity

class CreateGroupActivity : BaseActivity<ActivityCreateGroupBinding>(R.layout.activity_create_group) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.create_group_fcv) as NavHostFragment
        navHost.findNavController().setGraph(R.navigation.create_group_nav_graph, intent.extras)
    }
}