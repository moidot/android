package com.moidot.moidot.presentation.main.group.main.adater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moidot.moidot.data.remote.response.ResponseParticipateGroup
import com.moidot.moidot.databinding.ItemMyGroupBinding
import com.moidot.moidot.util.addVerticalMargin


class MyGroupAdapter(private val onItemClickListener: (ResponseParticipateGroup.Data) -> Unit) : RecyclerView.Adapter<MyGroupAdapter.MyGroupViewHolder>() {

    private var groups: List<ResponseParticipateGroup.Data> = listOf()

    class MyGroupViewHolder(
        private val binding: ItemMyGroupBinding,
        private val onItemClickListener: (ResponseParticipateGroup.Data) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ResponseParticipateGroup.Data) {
            binding.data = data
        }

        fun invokeItemClickListener(data: ResponseParticipateGroup.Data) {
            binding.root.setOnClickListener {
                data.groupParticipates
                onItemClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        val binding = ItemMyGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyGroupViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        addVerticalMargin(holder.itemView, position, itemCount, 8)
        holder.bind(groups[position])
        holder.invokeItemClickListener(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newData: List<ResponseParticipateGroup.Data>) {
        groups = newData
        notifyDataSetChanged()
    }
}
