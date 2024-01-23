package com.moidot.moidot.presentation.util.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ComponentTranportationPickerBinding

class TransportationPickerComponent(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private lateinit var binding: ComponentTranportationPickerBinding

    val isPublicSelected = MutableLiveData<Boolean>(false)
    val isCarSelected = MutableLiveData<Boolean>(false)
    val selectedTransportationTypeTxt = MutableLiveData<String>("")

    init {
        initBinding()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        binding.lifecycleOwner = lifecycleOwner
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.component_tranportation_picker, this, false
        )
        addView(binding.root)
        binding.layout = this
    }

    fun onPublicSelectedListener() {
        isPublicSelected.value = !isPublicSelected.value!!
        isCarSelected.value = false
        setSelectedTransportationTxt()
    }

    fun onCarSelectedListener() {
        isCarSelected.value = !isCarSelected.value!!
        isPublicSelected.value = false
        setSelectedTransportationTxt()
    }

    private fun setSelectedTransportationTxt() {
        selectedTransportationTypeTxt.value = when {
            isCarSelected.value == true -> "PERSONAL"
            isPublicSelected.value == true -> "PUBLIC"
            else -> ""
        }
    }
}