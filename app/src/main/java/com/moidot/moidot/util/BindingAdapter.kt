package com.moidot.moidot.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
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

@BindingAdapter("bind:InputEditTextFieldActive")
fun EditText.setInputEditTextFieldActive(textLength: Int) {
    val horizontalPadding = resources.getDimensionPixelSize(R.dimen.input_field_horizontal_padding)
    val verticalPadding = resources.getDimensionPixelSize(
        if (textLength > 0) R.dimen.input_field_active_vertical_padding
        else R.dimen.input_field_normal_vertical_padding
    )
    this.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    val currentTextStyle = if (textLength > 0) R.style.b2_reg_14 else R.style.c1_reg_12
    this.setTextAppearance(currentTextStyle)
}

@BindingAdapter("bind:InputTextFieldActive")
fun TextView.setInputTextFieldActive(flag: Boolean) {
    val horizontalPadding = resources.getDimensionPixelSize(R.dimen.input_field_horizontal_padding)
    val verticalPadding = resources.getDimensionPixelSize(
        if (flag) R.dimen.input_field_active_vertical_padding
        else R.dimen.input_field_normal_vertical_padding
    )
    this.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    val currentTextStyle = if (flag) R.style.b2_reg_14 else R.style.c1_reg_12
    val textColor = if (flag) R.color.gray800 else R.color.gray400
    this.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
    this.setTextAppearance(currentTextStyle)
}


@BindingAdapter("bind:setCheckBoxImage")
fun ImageView.setCheckBoxImage(checkedState: Boolean) {
    val checkBoxImage = if (checkedState) R.drawable.btn_checkbox_selected else R.drawable.btn_checkbox_normal
    this.setImageDrawable(AppCompatResources.getDrawable(this.context, checkBoxImage))
}

@BindingAdapter("bind:checkBoxTextFieldActive")
fun TextView.setCheckBoxTextFieldActive(checkedState: Boolean) {
    val currentTextStyle = if (checkedState) R.style.b2_bold_14 else R.style.b2_reg_14
    this.setTextAppearance(currentTextStyle)
}

@BindingAdapter("bind:TransportationType")
fun ImageView.transportationImageBind(type: String) {
    when (type) {
        "PUBLIC" -> Glide.with(this.context).load(R.drawable.ic_group_info_transportation).into(this)
        "PERSONAL" -> Glide.with(this.context).load(R.drawable.ic_group_info_car).into(this)
    }
}

@BindingAdapter("bind:leaderBadgeVisible")
fun ImageView.leaderBadgeVisible(flag: Boolean) {
    this.isVisible = flag
}

@BindingAdapter("bind:coverVisible")
fun View.coverVisible(flag: Boolean) {
    this.isVisible = flag
}