package com.moidot.moidot.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.moidot.moidot.databinding.CustomSnackBarBinding

object CustomSnackBar {
    @SuppressLint("ClickableViewAccessibility")
    fun makeSnackBar(view: View, message: String): Snackbar {
        val snackBar = Snackbar.make(view, message, 1000)
        setupSnackBarView(view, snackBar, message)
        return snackBar
    }

    private fun setupSnackBarView(view: View, snackBar: Snackbar, message: String) {
        val layoutInflater = LayoutInflater.from(view.context)
        val binding = CustomSnackBarBinding.inflate(layoutInflater, null, false)

        binding.customSnackBarTvMsg.text = message
        setupLayoutParams(snackBar)
        setupSnackBarLayout(snackBar, binding.root)
    }

    private fun setupLayoutParams(snackBar: Snackbar) {
        val params = snackBar.view.layoutParams
        when (params) {
            is CoordinatorLayout.LayoutParams -> {
                params.gravity = Gravity.TOP or Gravity.CENTER
                params.setMargins(0, 0, 0, 0)
            }

            is FrameLayout.LayoutParams -> {
                params.gravity = Gravity.TOP or Gravity.CENTER
                params.setMargins(0, 0, 0, 0)
            }
        }
        snackBar.view.layoutParams = params
    }

    private fun setupSnackBarLayout(snackBar: Snackbar, view: View) {
        (snackBar.view as Snackbar.SnackbarLayout).apply {
            setBackgroundColor(Color.TRANSPARENT)
            removeAllViews()
            addView(view)
        }
    }
}
