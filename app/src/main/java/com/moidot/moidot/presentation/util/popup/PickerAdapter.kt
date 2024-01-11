package com.moidot.moidot.presentation.util.popup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moidot.moidot.databinding.ItemPopupPickerBinding

class PickerAdapter(
    private val popupItems: List<String>,
    private val currentSelectedTxt: String,
    private val onSelectItem: (String) -> Unit
) : RecyclerView.Adapter<PickerAdapter.PickerViewHolder>() {

    class PickerViewHolder(
        private val binding: ItemPopupPickerBinding,
        private val currentSelectedTxt: String,
        private val onSelectItem: (String) -> Unit,
    ) : ViewHolder(binding.root) {
        init {
            onSelectItem(currentSelectedTxt)
        }

        fun bind(content: String) {
            binding.data = content
            binding.selectedTxt = currentSelectedTxt // 배경 활성화
            binding.root.setOnClickListener {
                onSelectItem(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val binding = ItemPopupPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickerViewHolder(binding, currentSelectedTxt, onSelectItem)
    }

    override fun getItemCount(): Int = popupItems.size

    override fun onBindViewHolder(holder: PickerViewHolder, position: Int) {
        holder.bind(popupItems[position])
    }
}