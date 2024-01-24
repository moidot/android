package com.moidot.moidot.presentation.ui.main.group.participate.view

import android.os.Bundle
import androidx.activity.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseSearchPlace
import com.moidot.moidot.databinding.ActivityParticipateGroupBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.ui.main.group.create.model.InputInfoType
import com.moidot.moidot.presentation.ui.main.group.participate.viewmodel.ParticipateGroupViewModel
import com.moidot.moidot.presentation.util.bottomsheet.BottomSheetLocationPicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipateGroupActivity: BaseActivity<ActivityParticipateGroupBinding>(R.layout.activity_participate_group) {

    private val viewModel: ParticipateGroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun onClickStartLocationPicker() {
        val locationPickerListener = object : BottomSheetLocationPicker.LocationPickerListener {
            override fun onSelectedItemListener(data: ResponseSearchPlace.Document) {
                viewModel.setLocationInfo(data)
                viewModel.updateInputInfoComplete(InputInfoType.LOCATION_INPUT, true)  // 위치 정보 입력 완료
            }
        }
        val bottomSheet = BottomSheetLocationPicker(locationPickerListener)
        bottomSheet.show(supportFragmentManager, "BottomSheetLocationPicker")
    }
}