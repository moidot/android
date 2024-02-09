package com.moidot.moidot.presentation.main.group.space.member.info.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoBinding
import com.moidot.moidot.util.addVerticalMargin

class MemberGroupInfoAdapter : RecyclerView.Adapter<MemberGroupInfoAdapter.MemberGroupInfoViewHolder>() {

    var members = listOf<ResponseGroupInfo.Data.ParticipantsByRegion.Participation>()
    var myName: String = ""

    class MemberGroupInfoViewHolder(private val binding: ItemGroupInfoBinding, private val myName:String) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion.Participation) {
            binding.data = data
        }

        fun setMyInfoView(data: ResponseGroupInfo.Data.ParticipantsByRegion.Participation) {
            if (myName == data.userName) {
                binding.itemGroupViewCover.visibility = View.VISIBLE
            } else {
                binding.itemGroupViewCover.visibility = View.INVISIBLE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberGroupInfoViewHolder {
        val binding = ItemGroupInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberGroupInfoViewHolder(binding, myName)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: MemberGroupInfoViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(members[position])
        holder.setMyInfoView(members[position])
    }

}