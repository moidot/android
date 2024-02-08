package com.moidot.moidot.presentation.main.group.space.leader.info.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoBinding
import com.moidot.moidot.util.addVerticalMargin
import com.moidot.moidot.util.popup.PopupTwoButtonDialog

class LeaderGroupInfoAdapter(private val onRemoveSelectListener: (Int) -> Unit) : RecyclerView.Adapter<LeaderGroupInfoAdapter.LeaderGroupInfoViewHolder>() {

    private var removeActivateFlag = false
    var members = listOf<ResponseGroupInfo.Data.ParticipantsByRegion.Participation>()
    var myName: String = ""

    class LeaderGroupInfoViewHolder(
        private val binding: ItemGroupInfoBinding,
        private val myName: String,
        private val removeActivateFlag: Boolean,
        private val onRemoveSelectListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
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

        fun setRemoveView(data: ResponseGroupInfo.Data.ParticipantsByRegion.Participation) {
            binding.itemGroupContainerRemoveMember.isVisible = removeActivateFlag && !data.isAdmin
            binding.itemGroupContainerMemberInfo.isVisible = !removeActivateFlag && !data.isAdmin
        }

        // 멤버 내보내기
        fun invokeItemRemoveListener(userName:String, participateId: Int) {
            binding.itemGroupContainerRemoveMember.setOnClickListener {
                PopupTwoButtonDialog(
                    it.context,
                    it.context.getString(R.string.space_leader_info_dialog_remove_member_title).format(userName),
                    it.context.getString(R.string.space_leader_info_dialog_remove_member_content),
                    it.context.getString(R.string.space_leader_info_dialog_remove_member_btn)
                ) { onRemoveSelectListener(participateId) }.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderGroupInfoViewHolder {
        val binding = ItemGroupInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderGroupInfoViewHolder(binding, myName, removeActivateFlag, onRemoveSelectListener)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: LeaderGroupInfoViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(members[position])
        holder.setMyInfoView(members[position])
        holder.setRemoveView(members[position])
        holder.invokeItemRemoveListener(members[position].userName, members[position].participationId)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRemoveFlag(flag: Boolean) {
        removeActivateFlag = flag
        notifyDataSetChanged()
    }

}