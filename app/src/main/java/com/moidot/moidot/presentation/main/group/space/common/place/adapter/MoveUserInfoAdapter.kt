package com.moidot.moidot.presentation.main.group.space.common.place.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.databinding.ItemMoveUserInfoBinding
import com.moidot.moidot.util.addVerticalMargin

class MoveUserInfoAdapter : ListAdapter<ResponseBestRegion.Data.MoveUserInfo, MoveUserInfoAdapter.BestRegionViewHolder>(bestRegionDiffUtil) {

    var myName: String = ""

    class BestRegionViewHolder(val binding: ItemMoveUserInfoBinding, private val myName:String) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseBestRegion.Data.MoveUserInfo) {
            binding.data = data
        }

        fun setMyInfoView(data: ResponseBestRegion.Data.MoveUserInfo) {
            if (myName == data.userName) {
                binding.itemMoveUserInfoViewCover.visibility = View.VISIBLE
            } else {
                binding.itemMoveUserInfoViewCover.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRegionViewHolder {
        val binding = ItemMoveUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestRegionViewHolder(binding, myName)
    }

    override fun onBindViewHolder(holder: BestRegionViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(getItem(position))
        holder.setMyInfoView(getItem(position))
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