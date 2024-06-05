package com.moidot.moidot.util.unit

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {
    fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return current.format(formatter)
    }

    fun convertToHoursAndMinutes(totalMinutes: Int): Pair<Int, Int> {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return Pair(hours, minutes)
    }
}