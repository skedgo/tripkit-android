package com.skedgo.tripkit.common.util

//To handle currency symbol if passed is words.
//Add more currency to handle
fun String.getCurrencySymbol(): String {
    return when {
        this == "USD" -> "$"
        this == "AUD" -> "AU$"
        else -> this
    }
}