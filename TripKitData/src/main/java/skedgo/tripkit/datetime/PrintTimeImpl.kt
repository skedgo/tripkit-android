package skedgo.tripkit.datetime

import android.content.Context
import android.text.format.DateFormat
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import rx.Observable
import rx.schedulers.Schedulers.computation
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

  override fun execute(dateTime: DateTime): Observable<String> {
    return Observable
        .fromCallable { this.printLocalTime(dateTime.toLocalTime()) }
        .subscribeOn(computation())
  }
}