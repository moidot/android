package com.moidot.moidot.presentation.ui.main.group.space.leader.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoHeaderBinding

class LeaderGroupInfoHeaderAdapter : RecyclerView.Adapter<LeaderGroupInfoHeaderAdapter.LeaderGroupInfoHeaderViewHolder>() {

    var participantsByRegion = mutableListOf<ResponseGroupInfo.Data.ParticipantsByRegion>()

    class LeaderGroupInfoHeaderViewHolder(private val binding: ItemGroupInfoHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion) {
            binding.data = data
        }

        fun initGroupInfoAdapter(members: ResponseGroupInfo.Data.ParticipantsByRegion?) {
            if (members == null) return
            val leaderGroupInfoAdapter = LeaderGroupInfoAdapter()
            leaderGroupInfoAdapter.members = members.participations
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

}