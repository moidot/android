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
import com.moidot.moidot.util.Constant.VOTE_RECREATE_STATE
import com.moidot.moidot.util.CustomSnackBar
import com.moidot.moidot.util.SpannableTxt
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


@AndroidEntryPoint
class CreateVoteActivity : BaseActivity<ActivityCreateVoteBinding>(R.layout.activity_create_vote) {

    private val viewModel: CreateVoteViewModel by viewModels()
    private val voteRecreateState by lazy { intent.getBooleanExtra(VOTE_RECREATE_STATE, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupObserver()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    // 종료 시간 추가 여부 체크
    fun endTimeCheckListener() {
        viewModel.setHasEndTime(!viewModel.hasEndTime.value!!)
    }

    fun setEndTimeInfoListener() {
        if (viewModel.hasEndTime.value == true) {
            showDatePicker()
        }
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
            val month = calendar.get(Calendar.MONTH) + 1
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            viewModel.setCurrentSelectedEndDate(year, month, dayOfMonth) // 날짜 선택 완료
            showTimePicker() // 시간 선택으로 이동
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    // 오늘 날짜로부터 일주일 후까지만 선택 가능하게 하기
    private fun getValidateDateRange(): CalendarConstraints.Builder {
        val minDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        // TODO 4시쯤에 다시 테스트해보는걸루....?
        val fromDate = DateValidatorPointForward.from(minDateCalendar.timeInMillis)
        minDateCalendar.add(Calendar.DAY_OF_MONTH, 7)
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

            if (viewModel.checkIsSameDate()) { // 같은 날짜 검사
                viewModel.checkIsValidTime(selectedHour, selectedMinute) // 시간 입력 유효성 검사
            } else {  // 정상 입력 완료 -> 시간차이 계산하기
                viewModel.calculateEndTimeDuration(selectedHour, selectedMinute)
            }
        }

        timePicker.show(supportFragmentManager, "timePicker")
    }

    // 복수투표 선택
    fun setMultipleSelectionCheckListener() {
        viewModel.setMultipleSelectionsCheckState(!viewModel.multipleSelectionsState.value!!)
    }

    // 익명투표 선택
    fun setAnonymousVoteCheckListener() {
        viewModel.setAnonymousVoteState(!viewModel.anonymousVoteState.value!!)
    }

    private fun setupObserver() {
        setHasEndTimeStateObserver()
        setValidateEndTimeObserver()
        setEndTimeTxtObserver()
        setEndTimeInputDoneStateObserver()
    }

    private fun setHasEndTimeStateObserver() {
        viewModel.hasEndTime.observe(this) {
            if (it && viewModel.endTimeInputDone.value == true) {
                viewModel.setBtnActiveState(true)
            } else if (it && viewModel.endTimeInputDone.value == false) {
                viewModel.setBtnActiveState(false)
            } else {
                viewModel.setBtnActiveState(true)
            }
        }
    }

    private fun setValidateEndTimeObserver() {
        viewModel.isValidateEndTime.observe(this) {
            if (!it) {
                CustomSnackBar.makeSnackBar(binding.root, TIME_ERROR_MSG).show()
            }
        }
    }

    private fun setEndTimeTxtObserver() {
        viewModel.endTimeTxt.observe(this) {
            if (viewModel.endTimeInputDone.value == true) { // 올바르게 입력된 시간은 spannable 처리를 해준다
                val target = it.substring(0, it.indexOf("분") + 1)
                val spannableTxt = SpannableTxt(this).applySpannableStyles(
                    content = it,
                    target = target,
                    styleResId = R.style.b2_bold_14
                )
                binding.createVoteTvSetEndTimeInput.text = spannableTxt
            } else {
                binding.createVoteTvSetEndTimeInput.text = it
            }
        }
    }

    private fun setEndTimeInputDoneStateObserver() {
        // 종료 입력 활성화 체크시 ->  종료 입력 체크
        viewModel.endTimeInputDone.observe(this) {
            when {
                it && viewModel.hasEndTime.value == true -> { viewModel.setBtnActiveState(true) }
                it && viewModel.hasEndTime.value == false -> { viewModel.setBtnActiveState(true) }
                !it && viewModel.hasEndTime.value == true -> { viewModel.setBtnActiveState(false) }
                !it && viewModel.hasEndTime.value == false -> { viewModel.setBtnActiveState(true) }
            }
        }
    }

    companion object {
        const val TIME_ERROR_MSG = "마감시간은 지금으로부터 최소 1시간 이후,\n최대 7일 이내로 설정할 수 있습니다."
    }
}