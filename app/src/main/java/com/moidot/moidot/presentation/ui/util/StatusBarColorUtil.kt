package com.moidot.moidot.presentation.ui.util

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class StatusBarColorUtil(private val activity: Activity) {

    fun setStatusBar(@ColorRes id: Int, iconType: String) {
        setStatusBarBgColor(id)
        setStatusBarIconColor(iconType)
    }

    private fun setStatusBarBgColor(@ColorRes id: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, id)
    }

    private fun setStatusBarIconColor(iconType: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 31이상
            activity.window.insetsController?.setSystemBarsAppearance(
                if (iconType == DARK_ICON_COLOR) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            activity.window.decorView.systemUiVisibility = if (iconType == DARK_ICON_COLOR) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
        }
    }

    companion object {
        const val LIGHT_ICON_COLOR = "light_icon_color"
        const val DARK_ICON_COLOR = "dark_icon_color"
    }
}