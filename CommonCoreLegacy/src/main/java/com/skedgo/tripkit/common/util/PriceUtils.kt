package com.skedgo.tripkit.common.util

fun Number.factor100() = this.toDouble() % 100.0 == 0.0

fun Double.decimalFormatWithCurrencySymbol(symbol: String): String =
    String.format("%s%.2f", symbol, this)

fun Int.nonDecimalFormatWithCurrencySymbol(symbol: String): String =
    String.format("%s%d", symbol, this.toInt())