package com.moidot.moidot.util.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.ItemLocationBinding

class BottomSheetLocationAdapter(
    private val onItemSelectListener: (ResponseSearchPlace.Document) -> Unit,
    private val onFavoriteSelectListener: (Int, ResponseSearchPlace.Document) -> Unit
) : RecyclerView.Adapter<BottomSheetLocationAdapter.LocationViewHolder>() {

    var savedFavorites = listOf<ResponseSearchPlace.Document>()
    private var placeItems = listOf<ResponseSearchPlace.Document>()

    fun setPlaceItems(newData: List<ResponseSearchPlace.Document>) {
        placeItems = newData
        notifyDataSetChanged()
    }

    class LocationViewHolder(
        private val binding: ItemLocationBinding,
        private val onItemSelectListener: (ResponseSearchPlace.Document) -> Unit,
        private val onFavoriteSelectListener: (Int, ResponseSearchPlace.Document) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseSearchPlace.Document) {
            binding.data = data
        }

        fun invokeItemSelectListener(data: ResponseSearchPlace.Document) {
            binding.itemLocationCvContainerRoot.setOnClickListener {
                (it as MaterialCardView).apply {
                    setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.orange100))
                    strokeColor = ContextCompat.getColor(it.context, R.color.orange500)
                }
                onItemSelectListener(data)
            }
        }

        fun invokeItemFavoriteListener(position: Int, data: ResponseSearchPlace.Document) {
            binding.itemLocationIvPin.setOnClickListener {
                onFavoriteSelectListener(position, data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemSelectListener, onFavoriteSelectListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(placeItems[position])
        holder.invokeItemSelectListener(placeItems[position])
        holder.invokeItemFavoriteListener(position, placeItems[position])
    }

    override fun getItemCount(): Int = placeItems.size

}
