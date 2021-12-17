package com.skedgo.tripkit

data class DateTimePickerConfig(
        val dateTimePickerLeaveAtLabel: String,
        val dateTimePickerArriveByLabel: String,
        val dateTimePickerMinuteInterval: Int = 1,
        val isWithLeaveNow: Boolean = true
)