package com.moidot.moidot.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ItemMarkerBestRegionBinding
import com.moidot.moidot.databinding.ItemMarkerMyBinding
import com.moidot.moidot.databinding.ItemMarkerOtherBinding

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
    fun getBestRegionPlaceMarker(name:String): Bitmap {
        val binding: ItemMarkerBestRegionBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_marker_best_region, null, false)
        binding.itemMarkerBestRegionTvName.text = name
        return createBitMapFromView(binding.root)
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