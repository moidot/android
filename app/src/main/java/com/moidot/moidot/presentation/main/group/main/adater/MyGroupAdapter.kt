package com.moidot.moidot.presentation.main.group.main.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.databinding.ItemMyGroupBinding
import com.moidot.moidot.util.addVerticalMargin


class MyGroupAdapter(
    private val onGroupItemClickListener: (ResponseParticipateGroup.Data) -> Unit,
    private val onGroupExistClickListener: (ResponseParticipateGroup.Data) -> Unit,
) :
    RecyclerView.Adapter<MyGroupAdapter.MyGroupViewHolder>() {

    private var isGroupExitMode = false
    private var groups: List<ResponseParticipateGroup.Data> = listOf()

    class MyGroupViewHolder(
        private val binding: ItemMyGroupBinding,
        private val onGroupItemClickListener: (ResponseParticipateGroup.Data) -> Unit,
        private val onGroupExitClickListener: (ResponseParticipateGroup.Data) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ResponseParticipateGroup.Data) {
            binding.data = data
        }

        fun setGroupExitMode(data: ResponseParticipateGroup.Data, isGroupExistMode: Boolean) {
            binding.itemMyGroupLlContainerExitMode.isVisible = isGroupExistMode && !data.isStartVote
            binding.itemMyGroupLlContainerCannotExitMode.isVisible = isGroupExistMode && data.isStartVote
        }

        fun invokeGroupItemClickListener(data: ResponseParticipateGroup.Data) {
            binding.root.setOnClickListener {
                onGroupItemClickListener(data)
            }
        }

        fun invokeGroupExitClickListener(data: ResponseParticipateGroup.Data) {
            binding.itemMyGroupLlContainerExitMode.setOnClickListener {
                onGroupExitClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        val binding = ItemMyGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyGroupViewHolder(binding, onGroupItemClickListener, onGroupExistClickListener)
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(groups[position])
        holder.setGroupExitMode(groups[position], isGroupExitMode)
        holder.invokeGroupItemClickListener(groups[position])
        holder.invokeGroupExitClickListener(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    fun updateItems(newData: List<ResponseParticipateGroup.Data>) {
        groups = newData
        notifyItemRangeChanged(0, itemCount)
    }

    fun setGroupExistModeOn() {
        isGroupExitMode = true
        notifyItemRangeChanged(0, itemCount)
    }

    fun setGroupExistModeOff() {
        isGroupExitMode = false
        notifyItemRangeChanged(0, itemCount)
    }
}
