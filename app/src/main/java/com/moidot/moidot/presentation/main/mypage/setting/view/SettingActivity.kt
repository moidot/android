package com.moidot.moidot.presentation.main.mypage.setting.view

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivitySettingBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.mypage.alarm.view.AlarmActivity
import com.moidot.moidot.presentation.main.mypage.policy.PolicyActivity
import com.moidot.moidot.presentation.main.mypage.policy.PolicyType
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.LOGOUT_SUCCESS
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.LOGOUT_FAIL
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.WITHDRAWAL_SUCCESS
import com.moidot.moidot.presentation.main.mypage.setting.model.UserState.WITHDRAWAL_FAIL
import com.moidot.moidot.presentation.main.mypage.setting.viewmodel.SettingViewModel
import com.moidot.moidot.presentation.sign.view.SignInActivity
import com.moidot.moidot.util.Constant.POLICY_TYPE_EXTRA
import com.moidot.moidot.util.Constant.SETTING_MSG_EXTRA
import com.moidot.moidot.util.popup.PopupTwoButtonDialog
import com.moidot.moidot.util.view.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupObserver()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun setupObserver() {
        viewModel.userState.observe(this) {
            when (it) {
                LOGOUT_SUCCESS -> moveToSignIn(getString(R.string.setting_msg_logout_success))

                LOGOUT_FAIL -> this.showToast(getString(R.string.setting_msg_logout_fail))

                WITHDRAWAL_SUCCESS -> moveToSignIn(getString(R.string.setting_msg_withdrawal_success))

                WITHDRAWAL_FAIL -> this.showToast(getString(R.string.setting_msg_withdrawal_fail))
            }
        }
    }

    private fun moveToSignIn(snackBarMsg: String) {
        Intent(this, SignInActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(SETTING_MSG_EXTRA, snackBarMsg)
            startActivity(this)
        }
        finish()
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
        ) {
            viewModel.withdrawal()
        }.show()
    }

    fun onClickServiceTerms() {
        Intent(this, PolicyActivity::class.java).apply {
            putExtra(POLICY_TYPE_EXTRA, PolicyType.ServiceTerms.name)
            startActivity(this)
        }
    }

    fun onClickPrivacyPolicy() {
        Intent(this, PolicyActivity::class.java).apply {
            putExtra(POLICY_TYPE_EXTRA, PolicyType.PrivacyPolicy.name)
            startActivity(this)
        }
    }

    fun onClickLocationTerms() {
        Intent(this, PolicyActivity::class.java).apply {
            putExtra(POLICY_TYPE_EXTRA, PolicyType.LocationTerms.name)
            startActivity(this)
        }
    }

    fun onClickAlarm() {
        Intent(this, AlarmActivity::class.java).apply {
            startActivity(this)
        }
    }
}