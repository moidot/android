package com.moidot.moidot.util.popup.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.databinding.ItemVotePeopleBinding

class VotePeopleAdapter(private val leaderName: String, private val people: List<String>) : RecyclerView.Adapter<VotePeopleAdapter.VotePeopleViewHolder>() {

    class VotePeopleViewHolder(private val leaderName: String, private val binding: ItemVotePeopleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String) {
            binding.itemVotePeopleIvLeader.isVisible = leaderName == name
            binding.itemVotePeopleTvName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotePeopleViewHolder {
        val binding = ItemVotePeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VotePeopleViewHolder(leaderName, binding)
    }

    override fun getItemCount(): Int = people.size

    override fun onBindViewHolder(holder: VotePeopleViewHolder, position: Int) {
        holder.bind(people[position])
    }

}