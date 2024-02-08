package com.moidot.moidot.presentation.main.group.space.leader.vote.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreateVoteViewModel @Inject constructor() : ViewModel() {

    private val _hasEndTime = MutableLiveData<Boolean>(false)
    val hasEndTime: LiveData<Boolean> = _hasEndTime

    private val _endTimeInputDone = MutableLiveData<Boolean>(false)
    val dateInputDone: LiveData<Boolean> = _endTimeInputDone

    private val _currentSelectedEndDate = MutableLiveData<String>()
    val currentSelectedEndDate: LiveData<String> = _currentSelectedEndDate

    private val _endTimeTxt = MutableLiveData<String>()
    val endTimeTxt: LiveData<String> = _endTimeTxt

    private val _isValidateEndTime = MutableSingleLiveData<Boolean>(true)
    val isValidateEndTime: SingleLiveData<Boolean> = _isValidateEndTime

    private val _multipleSelectionsState = MutableLiveData<Boolean>(false)
    val multipleSelectionsState: LiveData<Boolean> = _multipleSelectionsState

    private val _anonymousVoteState = MutableLiveData<Boolean>(false)
    val anonymousVoteState: LiveData<Boolean> = _anonymousVoteState

    fun setHasEndTime(flag: Boolean) {
        _hasEndTime.value = flag
    }

    fun setCurrentSelectedEndDate(year: Int, month: Int, day: Int) {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        _currentSelectedEndDate.value = LocalDate.of(year, month, day).format(formatter)
    }

    /** 마감일을 당일로 설정해놓았다면, 마감시간을 최소 1시간 이후로 설정했는지 체크해야한다.*/
    fun checkIsSameDate(): Boolean {
        val formattedTodayDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        if (_currentSelectedEndDate.value == formattedTodayDateTime) return true
        return false
    }

    // 시간차이 계산 함수
    fun checkIsValidTime(hour: Int, minute: Int) {
        val currentDateTime = LocalDateTime.now()
        val givenDateTime = currentDateTime.withHour(hour).withMinute(minute)
        if (givenDateTime.isAfter(currentDateTime)) {
            _isValidateEndTime.setValue(true)
            calculateEndTimeDuration(hour, minute)
        } else {
            _isValidateEndTime.setValue(false)
        }
    }

    fun calculateEndTimeDuration(hour: Int, minute: Int) {
        val selectedDateTime = _currentSelectedEndDate.value?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }!!.atTime(hour, minute)

        // 실질적으로 보이는 시간은 59분 후지만 사용자가 보이는 ui를 매끄럽게 하기 위해 1분을 딜레이시켰다.
        val currentDateTime = LocalDateTime.now().minusMinutes(1)
        val duration = Duration.between(currentDateTime, selectedDateTime)
        formatDuration(duration)
    }

    private fun formatDuration(duration: Duration) {
        val days = (duration.toHours() / 24).toInt()
        val hours = (duration.toHours() % 24).toInt()
        val minutes = (duration.toMinutes() % 60).toInt()

        _endTimeTxt.value = "${days}일 ${hours}시간 ${minutes}분 후에 투표가 종료됩니다."
        Log.d("kite", days.toString())
        Log.d("kite", hours.toString())
        Log.d("kite", minutes.toString())
    }


    fun setMultipleSelectionsCheckState(flag: Boolean) {
        _multipleSelectionsState.value = flag
    }

    fun setAnonymousVoteState(flag: Boolean) {
        _anonymousVoteState.value = flag
    }

}