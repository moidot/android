package com.moidot.moidot.presentation.ui.main.group.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.databinding.ItemMyGroupBinding


class MyGroupAdapter : ListAdapter<String, MyGroupAdapter.MyGroupViewHolder>(diffUtil) {

    class MyGroupViewHolder(private val binding: ItemMyGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        return MyGroupViewHolder(ItemMyGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}