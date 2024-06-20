package com.moidot.moidot.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.moidot.moidot.databinding.DialogLoadingBinding

class DialogUtil(context: Context) : Dialog(context) {
    private var binding: DialogLoadingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setDimAmount(0.2f)
        }
    }

    override fun show() {
        if(!this.isShowing) super.show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }
}