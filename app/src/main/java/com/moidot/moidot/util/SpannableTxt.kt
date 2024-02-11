package com.moidot.moidot.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan

class SpannableTxt(private val mContext: Context) {
    fun applySpannableStyles(content: String, target: String, color: Int? = null, styleResId: Int? = null): SpannableString {
        val spannableString = SpannableString(content)
        val startIdx = content.indexOf(target)
        val endIdx = startIdx + target.length

        spannableString.apply {
            if (color != null) setSpan(ForegroundColorSpan(mContext.resources.getColor(color, null)), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (styleResId != null) setSpan(TextAppearanceSpan(mContext, styleResId), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }
}