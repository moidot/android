package com.moidot.moidot.ui.main.group.myGroup.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.databinding.ItemMyGroupBinding
import com.moidot.moidot.util.addVerticalMargin


class MyGroupAdapter(private val onItemClickListener: (Boolean, Int, String) -> Unit) : ListAdapter<ResponseParticipateGroup.Data, MyGroupAdapter.MyGroupViewHolder>(diffUtil) {

    class MyGroupViewHolder(
        private val binding: ItemMyGroupBinding,
        private val onItemClickListener: (Boolean, Int, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseParticipateGroup.Data) {
            binding.data = data
        }

        fun invokeItemClickListener(data: ResponseParticipateGroup.Data) {
            binding.root.setOnClickListener {
                onItemClickListener(data.isAdmin, data.groupId, data.groupName)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        return MyGroupViewHolder(ItemMyGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(currentList[position])
        holder.invokeItemClickListener(currentList[position])
    }

    companion object {
        private val diffUtil = object : ItemCallback<ResponseParticipateGroup.Data>() {
            override fun areItemsTheSame(oldItem: ResponseParticipateGroup.Data, newItem: ResponseParticipateGroup.Data): Boolean {
                return oldItem.groupId == newItem.groupId
            }

            override fun areContentsTheSame(oldItem: ResponseParticipateGroup.Data, newItem: ResponseParticipateGroup.Data): Boolean {
                return oldItem == newItem
            }
        }
    }
}