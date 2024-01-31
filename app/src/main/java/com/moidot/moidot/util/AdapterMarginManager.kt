package com.moidot.moidot.util

import android.view.View
import android.view.ViewGroup

/** 리사이클러뷰 아이템 마진을 설정하기 위해 만든 kt파일
 * onBindViewHolder에 선언해주면 된다.*/
fun addVerticalMargin(view: View, position: Int, itemCount: Int, marginInDp: Int) {
    val layoutParams = (view.layoutParams as ViewGroup.MarginLayoutParams)
    layoutParams.bottomMargin = if (position == (itemCount - 1)) 0 else marginInDp.dpToPx(view.context)
    view.layoutParams = layoutParams
}