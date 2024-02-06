package com.moidot.moidot.util

import java.text.DecimalFormat

object PriceUtil {
    fun changePriceString(price: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(price)
    }
}