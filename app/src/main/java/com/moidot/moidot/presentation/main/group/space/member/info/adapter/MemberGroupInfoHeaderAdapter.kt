package com.moidot.moidot.presentation.main.group.space.member.info.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoHeaderBinding
import com.moidot.moidot.util.addVerticalMargin

class MemberGroupInfoHeaderAdapter : RecyclerView.Adapter<MemberGroupInfoHeaderAdapter.MemberGroupInfoHeaderViewHolder>() {

    private var userName: String = ""
    private var participantsByRegion = listOf<ResponseGroupInfo.Data.ParticipantsByRegion>()

    class MemberGroupInfoHeaderViewHolder(private val binding: ItemGroupInfoHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion) {
            binding.data = data
        }

        fun initGroupInfoAdapter(members: ResponseGroupInfo.Data.ParticipantsByRegion?, userName:String) {
            members?.let {
                val memberGroupInfoAdapter = MemberGroupInfoAdapter().apply {
                    this.members = it.participations
                    this.myName = userName
                }
                binding.itemGroupInfoHeaderRvGroupInfo.apply {
                    adapter = memberGroupInfoAdapter
                    itemAnimator = null
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberGroupInfoHeaderViewHolder {
        val binding = ItemGroupInfoHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberGroupInfoHeaderViewHolder(binding)
    }

    override fun getItemCount(): Int = participantsByRegion.size

    override fun onBindViewHolder(holder: MemberGroupInfoHeaderViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 24)
        holder.bind(participantsByRegion[position])
        holder.initGroupInfoAdapter(participantsByRegion[position], userName)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items:List<ResponseGroupInfo.Data.ParticipantsByRegion>) {
        participantsByRegion = items
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserInfo(name:String) {
        userName = name
        notifyDataSetChanged()
    }
}