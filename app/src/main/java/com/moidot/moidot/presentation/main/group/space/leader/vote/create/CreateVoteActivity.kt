package com.moidot.moidot.presentation.main.group.space.leader.vote.create

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityCreateVoteBinding
import com.moidot.moidot.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


@AndroidEntryPoint
class CreateVoteActivity : BaseActivity<ActivityCreateVoteBinding>(R.layout.activity_create_vote) {

    private val viewModel: CreateVoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    fun endTimeCheckListener() {
        viewModel.setHasEndTime(!viewModel.hasEndTime.value!!)
    }

    // yyyy-MM-ddTHH:mm:ss”
    fun setDateInfoListener() {
        if (viewModel.hasEndTime.value == true) {
            showDatePicker()
        }
    }

    fun setTimeInfoListener() {
        if (viewModel.hasEndTime.value == true) {
            showTimePicker()
        }
    }

    fun setMultipleSelectionCheckListener() {
        viewModel.setMultipleSelectionsCheckState(!viewModel.multipleSelectionsState.value!!)
    }

    fun setAnonymousVoteCheckListener() {
        viewModel.setAnonymousVoteState(!viewModel.anonymousVoteState.value!!)
    }

    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.custom_date_picker)
                .setTitleText(R.string.create_vote_date_picker_title)
                .build()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            calendar.time = Date(selectedDate)
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            // TODO 선택된 날짜에 대한 작업 수행
            Log.d("kite", "Selected date: $year-$month-$dayOfMonth")
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun showTimePicker() {

    }

}