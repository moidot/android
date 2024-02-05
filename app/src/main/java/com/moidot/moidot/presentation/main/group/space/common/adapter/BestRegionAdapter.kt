package com.moidot.moidot.presentation.main.group.space.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.databinding.ItemBestRegionBinding

class BestRegionAdapter : RecyclerView.Adapter<BestRegionAdapter.BestRegionViewHolder>() {

    private var regions = listOf<String>()

    class BestRegionViewHolder(private val binding: ItemBestRegionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, name: String) {
            binding.itemBestRegionTvNumber.text = position.plus(1).toString()
            binding.itemBestRegionTvName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRegionViewHolder {
        val binding = ItemBestRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestRegionViewHolder(binding)
    }

    override fun getItemCount(): Int = regions.size

    override fun onBindViewHolder(holder: BestRegionViewHolder, position: Int) {
        holder.bind(position, regions[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newData: List<String>) {
        regions = newData
        notifyDataSetChanged()
    }
}