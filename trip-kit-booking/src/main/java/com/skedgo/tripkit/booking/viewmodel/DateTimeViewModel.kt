package com.skedgo.tripkit.booking.viewmodel

interface DateTimeViewModel {
    fun getYear(): Int

    fun getMonth(): Int

    fun getDay(): Int

    fun getHour(): Int

    fun getMinute(): Int

    fun getTitle(): String

    fun getDate(): String

    fun getTime(): String
}