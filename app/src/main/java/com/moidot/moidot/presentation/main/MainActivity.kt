package com.moidot.moidot.presentation.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityMainBinding
import com.moidot.moidot.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomNav()
    }

    // TODO 추후 서비스가 확장될 때 바텀네비를 다시 추가한다.
    private fun initBottomNav() {
        // binding.mainBtmNav.itemIconTintList = null
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.main_fcv) as NavHostFragment? ?: return
        host.findNavController().setGraph(R.navigation.main_nav_graph, null)
        // val navController = host.navController
        // setupBottomNavMenu(navController)
    }

    /*private fun setupBottomNavMenu(navController: NavController) {
        binding.mainBtmNav.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener {
                NavigationUI.onNavDestinationSelected(it, navController)
                return@setOnItemSelectedListener true
            }
        }
    }*/
}