package com.moidot.moidot.util.popup.picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moidot.moidot.databinding.ItemPopupPickerBinding

class PickerAdapter(
    private val popupItems: List<String>,
    private var currentSelectedTxt: String,
    private val onSelectItem: (String) -> Unit
) : RecyclerView.Adapter<PickerAdapter.PickerViewHolder>() {

    class PickerViewHolder(
        private val binding: ItemPopupPickerBinding,
        private val onSelectItem: (String) -> Unit,
    ) : ViewHolder(binding.root) {

        fun bind(content: String, selectedTxt: String) {
            binding.data = content
            binding.selectedTxt = selectedTxt
            binding.root.setOnClickListener {
                onSelectItem(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val binding = ItemPopupPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickerViewHolder(binding, onSelectItem)
    }

    override fun getItemCount(): Int = popupItems.size

    override fun onBindViewHolder(holder: PickerViewHolder, position: Int) {
        holder.bind(popupItems[position], currentSelectedTxt)
    }

    fun updateCurrentSelectedTxt(newSelectedTxt: String) {
        currentSelectedTxt = newSelectedTxt
        notifyDataSetChanged()
    }
}