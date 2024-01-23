package com.moidot.moidot.presentation.ui.main.group.create.view

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityCreateGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.util.PermissionUtil
import com.moidot.moidot.presentation.util.PermissionUtil.Companion.LOCATION_CHECK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGroupActivity : BaseActivity<ActivityCreateGroupBinding>(R.layout.activity_create_group) {

    private lateinit var permissionUtil: PermissionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
        initPermissionUtil()
    }

    private fun initPermissionUtil() {
        permissionUtil = PermissionUtil(this, LOCATION_CHECK)
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.create_group_fcv) as NavHostFragment
        navHost.findNavController().setGraph(R.navigation.create_group_nav_graph, intent.extras)
    }

    fun getPermissionUtil(): PermissionUtil {
        return permissionUtil
    }
}