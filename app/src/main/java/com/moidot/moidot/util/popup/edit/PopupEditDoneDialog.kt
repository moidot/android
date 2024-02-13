package com.moidot.moidot.util.popup.edit

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.moidot.moidot.R
import com.moidot.moidot.databinding.PopupEditDoneDialogBinding

class PopupEditDoneDialog(
    context: Context,
    val nickName: String?,
    val location: String?,
    val transPortType: String?,
    val onClick: () -> Unit
) : Dialog(context, R.style.custom_dialog) {
    private lateinit var binding: PopupEditDoneDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        binding = PopupEditDoneDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dialog = this
    }

    private fun initView() {
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setCancelable(false)
        }
    }

    fun onClickConfirm() {
        onClick()
        dismiss()
    }
}