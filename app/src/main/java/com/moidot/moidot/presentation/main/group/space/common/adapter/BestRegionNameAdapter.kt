package com.moidot.moidot.presentation.main.group.space.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.databinding.ItemBestRegionNameBinding
import com.moidot.moidot.util.addHorizontalMargin

class BestRegionNameAdapter : RecyclerView.Adapter<BestRegionNameAdapter.BestRegionNameViewHolder>() {

    private var regions = listOf<String>()

    class BestRegionNameViewHolder(private val binding: ItemBestRegionNameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, name: String) {
            binding.itemBestRegionNameTvNumber.text = position.plus(1).toString()
            binding.itemBestRegionNameTvName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRegionNameViewHolder {
        val binding = ItemBestRegionNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestRegionNameViewHolder(binding)
    }

    override fun getItemCount(): Int = regions.size

    override fun onBindViewHolder(holder: BestRegionNameViewHolder, position: Int) {
        addHorizontalMargin(holder.itemView, position, itemCount, 12)
        holder.bind(position, regions[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newData: List<String>) {
        regions = newData
        notifyDataSetChanged()
    }
}