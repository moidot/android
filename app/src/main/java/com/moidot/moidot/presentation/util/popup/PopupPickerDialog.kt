package com.moidot.moidot.presentation.util.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.moidot.moidot.R
import com.moidot.moidot.databinding.PopupPickerDialogBinding
import com.moidot.moidot.presentation.util.dpToPx

class PopupPickerDialog(
    context: Context,
    val title: String,
    val popupItems: List<String>,
) : Dialog(context, R.style.custom_dialog) {
    private lateinit var binding: PopupPickerDialogBinding
    private lateinit var pickerAdapter: PickerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initAdapter()
    }

    private fun initBinding() {
        binding = PopupPickerDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dialog = this
    }

    private fun initView() {
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            decorView.setPadding(0, 0, 0, 40.dpToPx(context))
        }
    }

    private fun initAdapter() {
        pickerAdapter = PickerAdapter(popupItems)
        binding.popupPickerDialogRvItems.apply {
            setHasFixedSize(true)
            adapter = pickerAdapter
        }
    }
}