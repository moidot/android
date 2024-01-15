package com.moidot.moidot.presentation.util

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.moidot.moidot.R

@BindingAdapter("bind:urlImageBinding")
fun ImageView.imageBind(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}

@BindingAdapter("bind:drawableImageBinding")
fun ImageView.imageBind(drawable: Drawable?) {
    drawable?.let {
        Glide.with(this.context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("bind:InputTextFieldActive")
fun EditText.setInputTextFieldActive(textLength: Int) {
    val currentTextStyle = if (textLength > 0) R.style.b2_reg_14 else R.style.c1_reg_12
    this.setTextAppearance(currentTextStyle)
}