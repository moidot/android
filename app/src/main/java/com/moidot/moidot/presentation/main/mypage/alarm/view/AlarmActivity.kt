package com.moidot.moidot.presentation.main.mypage.alarm.view

import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityAlarmBinding
import com.moidot.moidot.presentation.base.BaseActivity

class AlarmActivity : BaseActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.fgInputLeaderInfoIvBack.setOnClickListener {
            finish()
        }
    }
}