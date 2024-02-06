package com.moidot.moidot.presentation.main.group.space.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.databinding.ItemBestRegionBinding
import com.moidot.moidot.util.addVerticalMargin

class BestRegionAdapter : ListAdapter<ResponseBestRegion.Data.MoveUserInfo, BestRegionAdapter.BestRegionViewHolder>(bestRegionDiffUtil) {

    class BestRegionViewHolder(val binding: ItemBestRegionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseBestRegion.Data.MoveUserInfo) {
            binding.data = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRegionViewHolder {
        val binding = ItemBestRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestRegionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestRegionViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(getItem(position))
    }

    companion object {
        private val bestRegionDiffUtil = object : DiffUtil.ItemCallback<ResponseBestRegion.Data.MoveUserInfo>() {
            override fun areItemsTheSame(
                oldItem: ResponseBestRegion.Data.MoveUserInfo,
                newItem: ResponseBestRegion.Data.MoveUserInfo
            ): Boolean =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(
                oldItem: ResponseBestRegion.Data.MoveUserInfo,
                newItem: ResponseBestRegion.Data.MoveUserInfo
            ): Boolean =
                oldItem == newItem
        }
    }
}