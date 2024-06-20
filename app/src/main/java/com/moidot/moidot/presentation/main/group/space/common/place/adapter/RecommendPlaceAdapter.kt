package com.moidot.moidot.presentation.main.group.space.common.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseRecommendPlace
import com.moidot.moidot.databinding.ItemRecommendPlaceBinding
import com.moidot.moidot.util.addVerticalMargin

class RecommendPlaceAdapter : ListAdapter<ResponseRecommendPlace.Data, RecommendPlaceAdapter.RecommendPlaceViewHolder>(recommendRegionDiffUtil) {

    class RecommendPlaceViewHolder(val binding: ItemRecommendPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseRecommendPlace.Data) {
            binding.data = data

            binding.itemRecommendTvEndTime.text = if (data.detail.status == "24시간 영업") {
                "24시간 영업"
            } else {
                data.openTime.ifEmpty { data.detail.openTime }
            }
            binding.itemRecommendTvTel.text = data.tel.ifEmpty { data.detail.tel }
            binding.itemRecommendImg.clipToOutline = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendPlaceViewHolder {
        val binding = ItemRecommendPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendPlaceViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 12)
        holder.bind(getItem(position))
    }

    companion object {
        private val recommendRegionDiffUtil = object : DiffUtil.ItemCallback<ResponseRecommendPlace.Data>() {
            override fun areItemsTheSame(
                oldItem: ResponseRecommendPlace.Data,
                newItem: ResponseRecommendPlace.Data
            ): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(
                oldItem: ResponseRecommendPlace.Data,
                newItem: ResponseRecommendPlace.Data
            ): Boolean =
                oldItem == newItem
        }
    }
}