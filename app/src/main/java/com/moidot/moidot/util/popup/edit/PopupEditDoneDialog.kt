package com.moidot.moidot.util.popup.edit

import android.app.Dialog
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.moidot.moidot.databinding.PopupEditDoneDialogBinding
import com.moidot.moidot.util.view.getScreenWidth

class PopupEditDoneDialog(
    val nickName: String?, val location: String?, val transPortType: String?, val onClick: () -> Unit
) : DialogFragment() {
    private var _binding: PopupEditDoneDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = PopupEditDoneDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val layoutParams = window.attributes
            layoutParams.width = (getScreenWidth(requireContext()) * 0.9).toInt() // 화면 너비의 90%
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initBinding()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(TRANSPARENT))
            isCancelable = false
        }
        return dialog
    }

    private fun initView() {
        if (nickName.isNullOrEmpty()) binding.popupEditDoneContainerNickname.isVisible = false
        if (location.isNullOrEmpty()) binding.popupEditDoneContainerLocation.isVisible = false
        if (transPortType.isNullOrEmpty()) binding.popupEditDoneContainerTransportType.isVisible = false
    }

    private fun initBinding() {
        binding.dialog = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClickConfirm() {
        onClick()
        dismiss()
    }
}