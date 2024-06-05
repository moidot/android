package com.moidot.moidot.presentation.main.group.space.member.vote.progress.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.databinding.ItemVoteProgressBinding
import com.moidot.moidot.util.addVerticalMargin

class VoteProgressInfoAdapter(private val onMemberShowClickListener: (Int, String) -> Unit) : RecyclerView.Adapter<VoteProgressInfoAdapter.VoteProgressInfoViewHolder>() {

    var voteState = false
    var checkBoxEnabledState = false
    var totalVoteNum = 0
    var progressStatuses = listOf<ResponseVoteStatus.Data.VoteStatuses>()
    var isMultipleChoice = false
    var isAnonymous = false

    class VoteProgressInfoViewHolder(private val binding: ItemVoteProgressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(totalVoteNum: Int, checkBoxEnabledState: Boolean, voteState: Boolean, data: ResponseVoteStatus.Data.VoteStatuses) {
            binding.data = data
            val percent = if (totalVoteNum == 0) 0 else ((data.votes / totalVoteNum.toFloat()) * 100).toInt()
            binding.itemVoteProgressPbStatus.progress = percent // 투표율
            binding.itemVoteProgressCbVote.isVisible = voteState // 투표 뷰 활성화 여부

            val checkBoxImg = if (data.isVoted) R.drawable.btn_checkbox_selected else R.drawable.btn_checkbox_normal
            Glide.with(binding.root.context).load(checkBoxImg).into(binding.itemVoteProgressCbVote)

            if (checkBoxEnabledState) {
                binding.itemVoteProgressCbVote.isEnabled = true
            } else {
                binding.itemVoteProgressCbVote.isEnabled = false
                val disabledCheckBoxImg = if (data.isVoted) R.drawable.btn_checkbox_selected else R.drawable.btn_checkbox_disabled
                Glide.with(binding.root.context).load(disabledCheckBoxImg).into(binding.itemVoteProgressCbVote)
            }
        }

        fun initPlaceVoteClickListener(
            data: ResponseVoteStatus.Data.VoteStatuses,
            onPlaceVoteClickListener: () -> Unit,
            addTotalVoteNum: () -> Unit,
            minusTotalVoteNum: () -> Unit,
        ) {
            binding.itemVoteProgressCbVote.setOnClickListener {
                binding.itemVoteProgressCbVote.isVisible = true
                data.isVoted = !data.isVoted
                data.votes = if (data.isVoted) data.votes + 1 else data.votes - 1
                if (data.isVoted) addTotalVoteNum() else minusTotalVoteNum()
                onPlaceVoteClickListener()
            }
        }

        fun invokeVoteMemberClickListener(
            isAnonymous: Boolean,
            bestPlaceId: Int,
            bestPlaceName: String,
            onMemberShowClickListener: (Int, String) -> Unit,
        ) {
            if (!isAnonymous) {
                binding.itemVoteProgressContainerPeopleInfo.setOnClickListener {
                    onMemberShowClickListener.invoke(bestPlaceId, bestPlaceName)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteProgressInfoViewHolder {
        val binding = ItemVoteProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteProgressInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = progressStatuses.size

    // TODO 복수투표
    override fun onBindViewHolder(holder: VoteProgressInfoViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 20)
        holder.bind(totalVoteNum, checkBoxEnabledState, voteState, progressStatuses[position])
        holder.initPlaceVoteClickListener(progressStatuses[position], ::onPlaceVoteClickListener, ::addTotalVoteNum, ::minusTotalVoteNum)
        holder.invokeVoteMemberClickListener(isAnonymous, progressStatuses[position].bestPlaceId, progressStatuses[position].placeName, onMemberShowClickListener)
    }

    fun updateVoteState(state: Boolean) {
        voteState = state
        notifyItemRangeChanged(0, itemCount)
    }

    fun updateCheckBoxEnableState(state: Boolean) {
        checkBoxEnabledState = state
        notifyItemRangeChanged(0, itemCount)
    }

    private fun onPlaceVoteClickListener() {
        notifyItemRangeChanged(0, itemCount)
    }

    private fun addTotalVoteNum() {
        totalVoteNum = totalVoteNum.plus(1)
    }

    private fun minusTotalVoteNum() {
        totalVoteNum = totalVoteNum.minus(1)
    }
}