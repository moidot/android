package com.moidot.moidot.presentation.main.mypage.notice.view

import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityNoticeDetailBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.util.Constant.NOTICE_CONTENT_EXTRA
import com.moidot.moidot.util.Constant.NOTICE_DATE_EXTRA
import com.moidot.moidot.util.Constant.NOTICE_TITLE_EXTRA

class NoticeDetailActivity : BaseActivity<ActivityNoticeDetailBinding>(R.layout.activity_notice_detail) {

    private val noticeTitle by lazy { intent.getStringExtra(NOTICE_TITLE_EXTRA) }
    private val noticeDate by lazy { intent.getStringExtra(NOTICE_DATE_EXTRA) }
    private val noticeContent by lazy { intent.getStringExtra(NOTICE_CONTENT_EXTRA) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButtonView()
        initNoticeInfoView()
    }

    private fun setupButtonView() {
        binding.noticeDetailIvBack.setOnClickListener {
            finish()
        }
    }

    private fun initNoticeInfoView() {
        binding.noticeDetailTvTitle.text = noticeTitle
        binding.noticeDetailTvDate.text = noticeDate
        binding.noticeDetailTvContent.text = noticeContent
    }
}