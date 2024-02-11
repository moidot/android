package com.moidot.moidot.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ItemMarkerBestRegionBinding
import com.moidot.moidot.databinding.ItemMarkerMyBinding
import com.moidot.moidot.databinding.ItemMarkerOtherBinding
import com.moidot.moidot.databinding.ItemMarkerVoteResultPlaceBinding

/** 커스텀 마커들을 생성하고 관리하는 클래스 */
class MarkerManager(private val mContext: Context) {

    // 모임장 위치 정보 마커 (디폴트 뷰)
    fun getMyPlaceMarker(name: String): Bitmap {
        val binding: ItemMarkerMyBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_marker_my, null, false)
        binding.itemMarkerMyTvName.text = name.first().toString() // 첫번째 글자만 따오기
        return createBitMapFromView(binding.root)
    }

    fun getOtherPlaceMarker(name: String): Bitmap {
        val binding: ItemMarkerOtherBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_marker_other, null, false)
        binding.itemMarkerOtherTvName.text = name.first().toString() // 첫번째 글자만 따오기
        return createBitMapFromView(binding.root)
    }

    // 추천 장소 마커
    fun getBestRegionPlaceMarker(name: String): Bitmap {
        val binding: ItemMarkerBestRegionBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_marker_best_region, null, false)
        binding.itemMarkerBestRegionTvName.text = name
        return createBitMapFromView(binding.root)
    }

    // 투표 결과 장소 마커
    // 비트맵으로 변환시 데이터 바인딩 안먹힘
    fun getVoteResultPlaceMarker(rank: Int, name: String): Bitmap {
        val binding: ItemMarkerVoteResultPlaceBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_marker_vote_result_place, null, false)
        binding.itemMarkerVoteResultPlaceTvPlace.text = name
        binding.itemMarkerVoteResultPlaceTvRank.text = rank.toString()
        setVoteResultMarkerView(rank, binding)
        return createBitMapFromView(binding.root)
    }

    private fun setVoteResultMarkerView(rank:Int, binding: ItemMarkerVoteResultPlaceBinding) {
        if (rank == 1) {
            binding.itemMarkerVoteResultPlaceIvPlace.setImageResource(R.drawable.ic_marker_rank)
            binding.itemMarkerVoteResultPlaceTvPlace.apply {
                setTextColor(resources.getColor(R.color.white, null))
                background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.bg_group_place_best_region_marker, null)
            }
            binding.itemMarkerVoteResultPlaceIvRank.isVisible = true
            binding.itemMarkerVoteResultPlaceTvRank.apply {
                background = ResourcesCompat.getDrawable(resources, R.drawable.bg_group_place_best_region_marker, null)
                setTextColor(resources.getColor(R.color.white, null))
                typeface =  Typeface.create(ResourcesCompat.getFont(mContext, R.font.pretendard_bold), Typeface.BOLD)
            }
        } else {
            binding.itemMarkerVoteResultPlaceIvPlace.setImageResource(R.drawable.ic_marker_unrank)
            binding.itemMarkerVoteResultPlaceTvPlace.apply {
                setTextColor(resources.getColor(R.color.orange500, null))
                background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.bg_group_place_best_region_marker_unrank, null)
            }
            binding.itemMarkerVoteResultPlaceIvRank.isVisible = false
            binding.itemMarkerVoteResultPlaceTvRank.apply {
                setTextColor(resources.getColor(R.color.orange500, null))
                typeface =  Typeface.create(ResourcesCompat.getFont(mContext, R.font.pretendard_regular), Typeface.NORMAL)
            }
        }
    }

    private fun createBitMapFromView(view: View): Bitmap {
        val displayMetrics = DisplayMetrics()

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

}