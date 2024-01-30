package com.moidot.moidot.ui.onboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.data.OnboardItem
import com.moidot.moidot.databinding.ItemOnboardBinding

class OnBoardingAdapter :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingHolder>() {
    var itemList = mutableListOf<OnboardItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingHolder {
        val binding = ItemOnboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    class OnBoardingHolder(private val binding: ItemOnboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OnboardItem) {
            binding.data = data
        }
    }
}