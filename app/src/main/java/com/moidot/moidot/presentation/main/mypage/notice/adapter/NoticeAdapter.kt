package com.moidot.moidot.presentation.main.mypage.notice.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.databinding.ItemNoticeBinding
import com.moidot.moidot.data.data.NoticeItemData
import com.moidot.moidot.data.data.noticeList
import com.moidot.moidot.presentation.main.mypage.notice.view.NoticeDetailActivity
import com.moidot.moidot.util.Constant.NOTICE_CONTENT_EXTRA
import com.moidot.moidot.util.Constant.NOTICE_DATE_EXTRA
import com.moidot.moidot.util.Constant.NOTICE_TITLE_EXTRA
import com.moidot.moidot.util.addVerticalMargin

class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private var notices: List<NoticeItemData> = noticeList

    class NoticeViewHolder(
        private val binding: ItemNoticeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: NoticeItemData) {
            binding.data = data
            binding.root.setOnClickListener {
                Intent(binding.root.context, NoticeDetailActivity::class.java).apply {
                    putExtra(NOTICE_TITLE_EXTRA, data.title)
                    putExtra(NOTICE_CONTENT_EXTRA, data.content)
                    putExtra(NOTICE_DATE_EXTRA, data.date)
                    binding.root.context.startActivity(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(notices[position])
    }

    override fun getItemCount(): Int = notices.size
}
