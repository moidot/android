package com.moidot.moidot.presentation.ui.main.group.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseMyGroupList
import com.moidot.moidot.databinding.ItemMyGroupBinding


class MyGroupAdapter : ListAdapter<ResponseMyGroupList.Data, MyGroupAdapter.MyGroupViewHolder>(diffUtil) {

    class MyGroupViewHolder(private val binding: ItemMyGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseMyGroupList.Data) {
            binding.data = data
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        return MyGroupViewHolder(ItemMyGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : ItemCallback<ResponseMyGroupList.Data>() {
            override fun areItemsTheSame(oldItem: ResponseMyGroupList.Data, newItem: ResponseMyGroupList.Data): Boolean {
                return oldItem.groupId == newItem.groupId
            }

            override fun areContentsTheSame(oldItem: ResponseMyGroupList.Data, newItem: ResponseMyGroupList.Data): Boolean {
                return oldItem == newItem
            }
        }
    }
}