package com.moidot.moidot.presentation.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.moidot.moidot.R
import com.moidot.moidot.presentation.util.popup.PopupTwoButtonDialog

class PermissionUtil(
    private val mActivity: AppCompatActivity,
    private val checkType: String,
) {

    var onPermissionGranted: () -> Unit = {}

    private val requestPermissionsLauncher = mActivity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when (checkType) {
            LOCATION_CHECK -> checkLocationRequestPermission(permissions)
        }
    }

    fun requestLocationPermission(onPermissionGranted: () -> Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissionsLauncher.launch(permissions)
        this@PermissionUtil.onPermissionGranted = onPermissionGranted
    }

    private fun checkLocationRequestPermission(permissions: Map<String, @JvmSuppressWildcards Boolean>) {
        val grantedFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val grantedCoarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (grantedFineLocation || grantedCoarseLocation) {
            onPermissionGranted()
        } else { // 권한 거부
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        fun doPositiveClick() {
            mActivity.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", mActivity.packageName, null)
                )
            )
        }

        val dialog = PopupTwoButtonDialog(
            mActivity,
            mActivity.getString(R.string.permission_dialog_location_title),
            mActivity.getString(R.string.permission_dialog_location_content),
            mActivity.getString(R.string.permission_dialog_ok),
        ) { doPositiveClick() }
        dialog.show()
    }

    companion object {
        const val LOCATION_CHECK = "LOCATION_CHECK"
    }
}