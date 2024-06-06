package com.moidot.moidot.presentation.main.mypage.policy

import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityPolicyBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.util.Constant.POLICY_TYPE_EXTRA

class PolicyActivity: BaseActivity<ActivityPolicyBinding>(R.layout.activity_policy) {

    private val policyType by lazy { intent.getStringExtra(POLICY_TYPE_EXTRA) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPolicyText()
    }

    private fun initPolicyText() {
        initPolicyCloseEvent()
        setupPolicyTitleText()
        setupPolicyContentText()
    }

    private fun initPolicyCloseEvent() {
        binding.policyIvBack.setOnClickListener {
            finish()
        }
    }

    private fun setupPolicyTitleText() {
        binding.policyTvTitle.text = when(policyType) {
            PolicyType.ServiceTerms.name -> getString(R.string.policy_service_terms_title)
            PolicyType.PrivacyPolicy.name -> getString(R.string.policy_privacy_policy_title)
            PolicyType.LocationTerms.name -> getString(R.string.policy_location_terms_title)
            else -> throw IllegalArgumentException("Cannot Found Policy Type")
        }
    }

    private fun setupPolicyContentText() {
        binding.policyTvContent.text = when(policyType) {
            PolicyType.ServiceTerms.name -> getString(R.string.policy_service_terms_content)
            PolicyType.PrivacyPolicy.name -> getString(R.string.policy_privacy_policy_content)
            PolicyType.LocationTerms.name -> getString(R.string.policy_location_terms_content)
            else -> throw IllegalArgumentException("Cannot Found Policy Type")
        }
    }

}