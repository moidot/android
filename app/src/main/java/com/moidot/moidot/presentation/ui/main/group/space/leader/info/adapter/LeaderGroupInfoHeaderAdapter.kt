package com.moidot.moidot.presentation.ui.main.group.space.leader.info.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoHeaderBinding
import com.moidot.moidot.presentation.util.VerticalSpaceItemDecoration
import com.moidot.moidot.presentation.util.dpToPx

class LeaderGroupInfoHeaderAdapter : RecyclerView.Adapter<LeaderGroupInfoHeaderAdapter.LeaderGroupInfoHeaderViewHolder>() {

    private var participantsByRegion = listOf<ResponseGroupInfo.Data.ParticipantsByRegion>()

    class LeaderGroupInfoHeaderViewHolder(private val binding: ItemGroupInfoHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion) {
            binding.data = data
        }

        fun initGroupInfoAdapter(members: ResponseGroupInfo.Data.ParticipantsByRegion?) {
            members?.let {
                val leaderGroupInfoAdapter = LeaderGroupInfoAdapter().apply {
                    this.members = it.participations
                }
                binding.itemGroupInfoHeaderRvGroupInfo.apply {
                    adapter = leaderGroupInfoAdapter
                    itemAnimator = null
                    addItemDecoration(VerticalSpaceItemDecoration(8.dpToPx(this.context)))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderGroupInfoHeaderViewHolder {
        val binding = ItemGroupInfoHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderGroupInfoHeaderViewHolder(binding)
    }

    override fun getItemCount(): Int = participantsByRegion.size

    override fun onBindViewHolder(holder: LeaderGroupInfoHeaderViewHolder, position: Int) {
        holder.bind(participantsByRegion[position])
        holder.initGroupInfoAdapter(participantsByRegion[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items:List<ResponseGroupInfo.Data.ParticipantsByRegion>) {
        participantsByRegion = items
        notifyDataSetChanged()
    }

}