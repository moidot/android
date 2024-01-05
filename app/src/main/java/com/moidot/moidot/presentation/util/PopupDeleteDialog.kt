package com.moidot.moidot.presentation.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.moidot.moidot.R
import com.moidot.moidot.databinding.PopupDeleteDialogBinding

class PopupDeleteDialog(
    context: Context,
    val title: String,
    val content: String,
    val btnTxt: String,
    val onClick: () -> Unit
) : Dialog(context, R.style.custom_dialog) {
    private lateinit var binding: PopupDeleteDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding = PopupDeleteDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dialog = this
    }

    private fun initView() {
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun onClickDelete() {
        onClick()
        dismiss()
    }
}