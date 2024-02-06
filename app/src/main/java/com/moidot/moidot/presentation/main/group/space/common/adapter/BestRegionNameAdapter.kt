package com.moidot.moidot.presentation.main.group.space.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.data.BestRegionItem
import com.moidot.moidot.databinding.ItemBestRegionNameBinding

class BestRegionNameAdapter : RecyclerView.Adapter<BestRegionNameAdapter.BestRegionNameViewHolder>() {

    private var regions = listOf<BestRegionItem>()

    class BestRegionNameViewHolder(private val binding: ItemBestRegionNameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, data: BestRegionItem) {
            binding.data = data
            binding.itemBestRegionNameTvNumber.text = position.plus(1).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRegionNameViewHolder {
        val binding = ItemBestRegionNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestRegionNameViewHolder(binding)
    }

    override fun getItemCount(): Int = regions.size

    override fun onBindViewHolder(holder: BestRegionNameViewHolder, position: Int) {
        holder.bind(position, regions[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newData: List<BestRegionItem>) {
        regions = newData
        notifyDataSetChanged()
    }

    // UI 업데이트를 위한 isSelected 변수 변경
    fun updateSelectedPosition(position: Int) {
        regions.forEachIndexed { index, _ ->
            regions[index].isSelected = (index == position)
        }
        notifyItemRangeChanged(0, regions.size)
    }
}