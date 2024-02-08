package com.moidot.moidot.presentation.main.group.space.leader.vote.create

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityCreateVoteBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.util.bottomsheet.BottomSheetNumberPicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.max


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
    fun setEndTimeInfoListener() {
        if (viewModel.hasEndTime.value == true) {
            // BottomSheetNumberPicker().show(supportFragmentManager, "numberpicker")
            showDatePicker()
        }
    }

    fun setMultipleSelectionCheckListener() {
        viewModel.setMultipleSelectionsCheckState(!viewModel.multipleSelectionsState.value!!)
    }

    fun setAnonymousVoteCheckListener() {
        viewModel.setAnonymousVoteState(!viewModel.anonymousVoteState.value!!)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(getValidateDateRange().build())
                .setTheme(R.style.custom_date_picker)
                .setTitleText(R.string.create_vote_date_picker_title)
                .build()
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

    // 오늘 날짜로부터 일주일 후까지만 선택 가능하게 하기
    private fun getValidateDateRange(): CalendarConstraints.Builder{
        val minDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        minDateCalendar.add(Calendar.DAY_OF_MONTH, -1)
        val fromDate = DateValidatorPointForward.from(minDateCalendar.timeInMillis)
        minDateCalendar.add(Calendar.DAY_OF_MONTH, 8)
        val toDate = DateValidatorPointBackward.before(minDateCalendar.timeInMillis)

        val validators = CompositeDateValidator.allOf(arrayListOf(fromDate, toDate))
        return CalendarConstraints.Builder().setValidator(validators)
    }

    private fun showTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTheme(R.style.custom_time_picker)
                .setTitleText(R.string.create_vote_time_picker_title)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute

            // TODO 선택된 시간에 대한 작업 수행
            Log.d("kite", "Selected time: $selectedHour:$selectedMinute")
        }

        timePicker.show(supportFragmentManager, "timePicker")
    }

}