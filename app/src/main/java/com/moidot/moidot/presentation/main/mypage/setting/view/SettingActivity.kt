package com.moidot.moidot.presentation.main.mypage.setting.view

import android.os.Bundle
import androidx.activity.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivitySettingBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.mypage.setting.viewmodel.SettingViewModel
import com.moidot.moidot.util.popup.PopupTwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    fun onClickLogout() {
        viewModel.logout()
    }

    fun onClickWithdrawal() {
        PopupTwoButtonDialog(
            this,
            getString(R.string.setting_withdrawal_dialog_title),
            getString(R.string.setting_withdrawal_dialog_content),
            getString(R.string.setting_withdrawal_dialog_btn)
        ) { }.show()
    }

}