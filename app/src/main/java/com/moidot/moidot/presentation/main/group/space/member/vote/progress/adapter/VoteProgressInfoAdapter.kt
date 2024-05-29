package com.moidot.moidot.presentation.main.group.space.member.vote.progress.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.databinding.ItemVoteProgressBinding
import com.moidot.moidot.util.addVerticalMargin

class VoteProgressInfoAdapter : RecyclerView.Adapter<VoteProgressInfoAdapter.VoteProgressInfoViewHolder>() {

    private var voteState = false // 투표 선택 활성화 변수
    var totalVoteNum = 0
    var progressStatuses = listOf<ResponseVoteStatus.Data.VoteStatuses>()

    class VoteProgressInfoViewHolder(private val voteState: Boolean, private val totalVoteNum: Int, private val binding: ItemVoteProgressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseVoteStatus.Data.VoteStatuses) {
            binding.data = data
            val percent = if (totalVoteNum == 0) 0 else data.votes / totalVoteNum
            binding.itemVoteProgressPbStatus.progress = percent
            binding.itemVoteProgressCbVote.isVisible = voteState
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteProgressInfoViewHolder {
        val binding = ItemVoteProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteProgressInfoViewHolder(voteState, totalVoteNum, binding)
    }

    override fun getItemCount(): Int = progressStatuses.size

    override fun onBindViewHolder(holder: VoteProgressInfoViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 20)
        holder.bind(progressStatuses[position])
    }

    fun getVoteState(): Boolean {
        return voteState
    }

    fun updateVoteStateTrue() {
        voteState = !voteState
        notifyItemRangeChanged(0, itemCount)
    }

}