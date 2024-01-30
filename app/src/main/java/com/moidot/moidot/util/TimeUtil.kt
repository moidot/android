package com.moidot.moidot.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {
    fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return current.format(formatter)
    }
}