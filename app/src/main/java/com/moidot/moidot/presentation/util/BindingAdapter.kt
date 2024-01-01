package com.moidot.moidot.presentation.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

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