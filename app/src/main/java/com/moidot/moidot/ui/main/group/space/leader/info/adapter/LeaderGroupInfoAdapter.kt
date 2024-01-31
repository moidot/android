package com.moidot.moidot.ui.main.group.space.leader.info.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseGroupInfo
import com.moidot.moidot.databinding.ItemGroupInfoBinding
import com.moidot.moidot.util.addVerticalMargin

class LeaderGroupInfoAdapter(private val onRemoveSelectListener: (Int) -> Unit) : RecyclerView.Adapter<LeaderGroupInfoAdapter.LeaderGroupInfoViewHolder>() {

    private var removeActivateFlag = false
    var members = listOf<ResponseGroupInfo.Data.ParticipantsByRegion.Participation>()

    class LeaderGroupInfoViewHolder(
        private val binding: ItemGroupInfoBinding,
        private val removeActivateFlag: Boolean,
        private val onRemoveSelectListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGroupInfo.Data.ParticipantsByRegion.Participation) {
            binding.data = data
        }

        fun setRemoveView() { // TODO 모임장 본인은 내보내기 뷰 활성화 못하게 막음, 추후 서버 데이터 연동시 수정 예정
            binding.itemGroupContainerRemoveMember.isVisible = removeActivateFlag
            binding.itemGroupContainerMemberInfo.isVisible = !removeActivateFlag
        }

        // 멤버 내보내기
        fun invokeItemRemoveListener(participateId: Int) {
            binding.itemGroupContainerRemoveMember.setOnClickListener {
                onRemoveSelectListener(participateId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderGroupInfoViewHolder {
        val binding = ItemGroupInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderGroupInfoViewHolder(binding, removeActivateFlag, onRemoveSelectListener)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: LeaderGroupInfoViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(members[position])
        holder.setRemoveView()
        holder.invokeItemRemoveListener(members[position].participationId)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRemoveFlag(flag: Boolean) {
        removeActivateFlag = flag
        notifyDataSetChanged()
    }

}