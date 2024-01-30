package com.moidot.moidot.ui.main.group.space.leader.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoBinding

class LeaderGroupInfoAdapter : RecyclerView.Adapter<LeaderGroupInfoAdapter.LeaderGroupInfoViewHolder>() {

    var members = listOf<ResponseGroupInfo.Data.ParticipantsByRegion.Participation>()

    class LeaderGroupInfoViewHolder(private val binding: ItemGroupInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion.Participation) {
            binding.data = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderGroupInfoViewHolder {
        val binding = ItemGroupInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderGroupInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: LeaderGroupInfoViewHolder, position: Int) {
        holder.bind(members[position])
    }

}