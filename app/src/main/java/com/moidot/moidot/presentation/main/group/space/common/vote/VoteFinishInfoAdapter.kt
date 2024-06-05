package com.moidot.moidot.presentation.main.group.space.common.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseVoteStatus
import com.moidot.moidot.databinding.ItemVoteFinishBinding
import com.moidot.moidot.util.addVerticalMargin

class VoteFinishInfoAdapter(private val onMemberShowClickListener: (Int, String) -> Unit) : RecyclerView.Adapter<VoteFinishInfoAdapter.VoteFinishInfoViewHolder>() {

    var isAnonymous = false
    var totalVoteNum = 0
    var progressStatuses = listOf<ResponseVoteStatus.Data.VoteStatuses>()

    class VoteFinishInfoViewHolder(private val binding: ItemVoteFinishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(totalVoteNum: Int, getFirstRankVoteCount: Int, data: ResponseVoteStatus.Data.VoteStatuses) {
            binding.data = data
            val percent = if (totalVoteNum == 0) 0 else ((data.votes / totalVoteNum.toFloat()) * 100).toInt()
            binding.itemVoteFinishPbStatus.progress = percent // 투표율

            // 해당 장소에 투표 했는지 여부 표시
            val checkBoxImg = if (data.isVoted) R.drawable.btn_checkbox_selected else R.drawable.btn_checkbox_normal
            Glide.with(binding.root.context).load(checkBoxImg).into(binding.itemVoteFinishCbVote)

            // 해당 장소가 1등인지 여부 표시
            binding.itemVoteFinishIvRanker.isVisible = data.votes == getFirstRankVoteCount
        }

        fun invokeVoteMemberClickListener(
            isAnonymous: Boolean,
            bestPlaceId: Int,
            bestPlaceName: String,
            onMemberShowClickListener: (Int, String) -> Unit,
        ) {
            if (!isAnonymous) {
                binding.itemVoteFinishContainerPeopleInfo.setOnClickListener {
                    onMemberShowClickListener.invoke(bestPlaceId, bestPlaceName)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteFinishInfoViewHolder {
        val binding = ItemVoteFinishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteFinishInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = progressStatuses.size

    override fun onBindViewHolder(holder: VoteFinishInfoViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 20)
        holder.bind(totalVoteNum, getFirstRankVoteCount(), progressStatuses[position])
        holder.invokeVoteMemberClickListener(isAnonymous, progressStatuses[position].bestPlaceId, progressStatuses[position].placeName, onMemberShowClickListener)
    }

    private fun getFirstRankVoteCount(): Int {
        return progressStatuses.maxOfOrNull { it.votes } ?: 0
    }
}