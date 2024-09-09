package com.skedgo.tripkit.datetime

import io.reactivex.Flowable
import org.joda.time.DateTime
import org.joda.time.LocalTime

/**
 * An UseCase to print time with respect of 24-hour
 * (or 12-hour) setting on users' device.
 */
interface PrintTime {
    fun execute(dateTime: DateTime): Flowable<String>
    fun printLocalTime(localTime: LocalTime): String
    fun print(dateTime: DateTime): String
}
