package com.moidot.moidot.presentation.main.group.space.leader.info.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoHeaderBinding
import com.moidot.moidot.util.addVerticalMargin

class LeaderGroupInfoHeaderAdapter(private val onRemoveSelectListener: (Int) -> Unit) : RecyclerView.Adapter<LeaderGroupInfoHeaderAdapter.LeaderGroupInfoHeaderViewHolder>() {

    private var userName: String = ""
    private var removeActivateFlag = false
    private var participantsByRegion = listOf<ResponseGroupInfo.Data.ParticipantsByRegion>()

    class LeaderGroupInfoHeaderViewHolder(private val binding: ItemGroupInfoHeaderBinding, private val onRemoveSelectListener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion) {
            binding.data = data
        }

        fun initGroupInfoAdapter(members: ResponseGroupInfo.Data.ParticipantsByRegion?, removeActivateFlag: Boolean, userName:String) {
            members?.let {
                val leaderGroupInfoAdapter = LeaderGroupInfoAdapter(onRemoveSelectListener).apply {
                    this.members = it.participations
                    this.myName = userName
                    setRemoveFlag(removeActivateFlag)
                }
                binding.itemGroupInfoHeaderRvGroupInfo.apply {
                    adapter = leaderGroupInfoAdapter
                    itemAnimator = null
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderGroupInfoHeaderViewHolder {
        val binding = ItemGroupInfoHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderGroupInfoHeaderViewHolder(binding, onRemoveSelectListener)
    }

    override fun getItemCount(): Int = participantsByRegion.size

    override fun onBindViewHolder(holder: LeaderGroupInfoHeaderViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 24)
        holder.bind(participantsByRegion[position])
        holder.initGroupInfoAdapter(participantsByRegion[position], removeActivateFlag, userName)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<ResponseGroupInfo.Data.ParticipantsByRegion>) {
        participantsByRegion = items
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRemoveMode(flag: Boolean) {
        removeActivateFlag = flag
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserInfo(name:String) {
        userName = name
        notifyDataSetChanged()
    }

}