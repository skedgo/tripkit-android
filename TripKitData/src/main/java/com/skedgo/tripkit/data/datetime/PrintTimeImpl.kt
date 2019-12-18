package com.skedgo.tripkit.data.datetime

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import skedgo.tripkit.datetime.PrintTime
import java.util.*

internal class PrintTimeImpl constructor(
    private val context: Context
) : PrintTime {
  private val patternFor24Hour = "H:mm"
  private val patternFor12Hour = "h:mm a"

  override fun printLocalTime(localTime: LocalTime): String {
    val formatter = if (DateFormat.is24HourFormat(context)) {
      DateTimeFormat.forPattern(patternFor24Hour).withLocale(Locale.getDefault())
    } else {
      DateTimeFormat.forPattern(patternFor12Hour).withLocale(Locale.getDefault())
    }
    return formatter.print(localTime)
  }

  override fun execute(dateTime: DateTime): Flowable<String> {
    return Flowable
        .fromCallable { this.printLocalTime(dateTime.toLocalTime()) }
        .subscribeOn(Schedulers.computation())
  }

  override fun print(dateTime: DateTime): String {
    return this.printLocalTime(dateTime.toLocalTime())
  }
}