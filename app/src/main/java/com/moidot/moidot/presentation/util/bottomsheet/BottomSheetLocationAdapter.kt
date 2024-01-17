package com.moidot.moidot.presentation.util.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.ItemLocationBinding

class BottomSheetLocationAdapter(
    private val onItemSelected: (ResponseSearchPlace.Document) -> Unit
) : ListAdapter<ResponseSearchPlace.Document, BottomSheetLocationAdapter.LocationViewHolder>(diffUtil) {

    class LocationViewHolder(
        private val binding: ItemLocationBinding, private val onItemSelected: (ResponseSearchPlace.Document) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseSearchPlace.Document) {
            binding.data = data
            binding.itemLocationCvContainerRoot.setOnClickListener {
                (it as MaterialCardView).apply {
                    setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.orange100))
                    strokeColor = ContextCompat.getColor(it.context, R.color.orange500)
                }
                onItemSelected(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemSelected)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ResponseSearchPlace.Document>() {
            override fun areItemsTheSame(oldItem: ResponseSearchPlace.Document, newItem: ResponseSearchPlace.Document): Boolean {
                return oldItem.placeName == newItem.placeName
            }

            override fun areContentsTheSame(oldItem: ResponseSearchPlace.Document, newItem: ResponseSearchPlace.Document): Boolean {
                return oldItem == newItem
            }
        }
    }
}