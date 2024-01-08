package com.moidot.moidot.presentation.util.popup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moidot.moidot.databinding.ItemPopupPickerBinding

class PickerAdapter(
    private val popupItems: List<String>
) : RecyclerView.Adapter<PickerAdapter.PickerViewHolder>() {
    class PickerViewHolder(
        private val binding: ItemPopupPickerBinding,
    ) : ViewHolder(binding.root) {
        fun bind(content: String) {
            binding.data = content
            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val binding = ItemPopupPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickerViewHolder(binding)
    }

    override fun getItemCount(): Int = popupItems.size

    override fun onBindViewHolder(holder: PickerViewHolder, position: Int) {
        holder.bind(popupItems[position])
    }
}