package com.moidot.moidot.presentation.main.mypage.notice.view

import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityNoticeBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.mypage.notice.adapter.NoticeAdapter

class NoticeActivity : BaseActivity<ActivityNoticeBinding>(R.layout.activity_notice) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButtonView()
        initAdapter()
    }

    private fun setupButtonView() {
        binding.noticeIvBack.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        binding.noticeRvNotice.adapter = NoticeAdapter()
    }

}
