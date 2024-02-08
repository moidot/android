package com.moidot.moidot.presentation.main.group.space.leader.vote.create

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moidot.moidot.util.event.MutableSingleLiveData
import com.moidot.moidot.util.event.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId.systemDefault
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreateVoteViewModel @Inject constructor() : ViewModel() {

    private val _hasEndTime = MutableLiveData<Boolean>(false)
    val hasEndTime: LiveData<Boolean> = _hasEndTime

    // 종료 시간 입력 완료
    private val _endTimeInputDone = MutableLiveData<Boolean>(false)
    val endTimeInputDone: LiveData<Boolean> = _endTimeInputDone

    // 마지막으로 선택한 종료 시간
    private val _currentSelectedEndDate = MutableLiveData<String>()
    val currentSelectedEndDate: LiveData<String> = _currentSelectedEndDate

    // 서버에 보내줄 종료 시간 - yyyy-MM-ddTHH:mm:ss” 형태로 서버에 보내주어야 한다.
    private val selectedEndDateTime = MutableLiveData<String>()

    // 뷰에서 사용자에게 보여질 종료 예정까지 남은 시간
    private var countDownTimer: CountDownTimer? = null

    private val _endTimeTxt = MutableLiveData<String>()
    val endTimeTxt: LiveData<String> = _endTimeTxt

    // 종료 시간 유효성 체크
    private val _isValidateEndTime = MutableSingleLiveData<Boolean>(true)
    val isValidateEndTime: SingleLiveData<Boolean> = _isValidateEndTime

    private val _multipleSelectionsState = MutableLiveData<Boolean>(false)
    val multipleSelectionsState: LiveData<Boolean> = _multipleSelectionsState

    private val _anonymousVoteState = MutableLiveData<Boolean>(false)
    val anonymousVoteState: LiveData<Boolean> = _anonymousVoteState

    private val _btnActiveState = MutableLiveData<Boolean>(true)
    val btnActiveState: LiveData<Boolean> = _btnActiveState

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

    // 시간 차이 계산 함수
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
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        } // 이전에 호출했던 것이 남아있으면 종료시킨다.

        val selectedDateTime = _currentSelectedEndDate.value?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }!!.atTime(hour, minute)

        val remainingMillis = selectedDateTime.atZone(systemDefault()).toInstant().toEpochMilli()
        fun setTimeDurationInfo() {
            val currentDateTime = LocalDateTime.now().minusMinutes(1)
            val remainingDuration = Duration.between(currentDateTime, selectedDateTime)
            formatDuration(remainingDuration)
        }

        // 실질적으로 보이는 시간은 59분 후지만 사용자가 보이는 ui를 매끄럽게 하기 위해 1분을 딜레이시켰다.
        if (hasEndTime.value == true) {
            // 분 단위로 ui에 보이는 숫자를 갱신시킨다.
            setTimeDurationInfo() // UI 에 Spannable을 그리기 위해 카운트 다운 시작 전 최초로 한 번 먼저 값을 넣어 준다.
            countDownTimer = object : CountDownTimer(remainingMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    setTimeDurationInfo()
                }

                override fun onFinish() {
                }
            }.start()
        }
    }

    private fun formatDuration(duration: Duration) {
        val days = (duration.toHours() / 24).toInt()
        val hours = (duration.toHours() % 24).toInt()
        val minutes = (duration.toMinutes() % 60).toInt()

        if (days == 0 && hours == 0 && minutes <= 59) {
            _endTimeTxt.value = "최소 1시간 이후부터 투표가 가능합니다."
            _endTimeInputDone.value = false
            _hasEndTime.value = false
            countDownTimer!!.cancel() // 잘못된 시간으로 변경되면 카운트 다운이 종료되어야 한다.
        } else {
            _endTimeTxt.value = "${days}일 ${hours}시간 ${minutes}분 후에 투표가 종료됩니다."
            _endTimeInputDone.value = true
        }
    }


    fun setMultipleSelectionsCheckState(flag: Boolean) {
        _multipleSelectionsState.value = flag
    }

    fun setAnonymousVoteState(flag: Boolean) {
        _anonymousVoteState.value = flag
    }

    fun setBtnActiveState(flag: Boolean) {
        _btnActiveState.value = flag
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }

}