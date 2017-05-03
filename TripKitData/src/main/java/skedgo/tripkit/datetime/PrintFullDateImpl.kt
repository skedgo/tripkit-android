package skedgo.tripkit.datetime

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import rx.Observable
import rx.schedulers.Schedulers
import java.text.DateFormat
import java.util.*

internal class PrintFullDateImpl constructor(
    private val getLocale: () -> Locale = { Locale.getDefault() },
    private val printFullDateByJoda: (DateTime, Locale) -> String = {
      dateTime, locale ->
      DateTimeFormat.fullDate().withLocale(locale).print(dateTime)
    },
    private val printFullDateByJava: (Date, Locale) -> String = {
      date, locale ->
      DateFormat.getDateInstance(DateFormat.FULL, locale).format(date)
    }
) : PrintFullDate {
  override fun execute(dateTime: DateTime): Observable<String>
      = Observable
      .fromCallable {
        // Still relies on Joda because of its up-to-date timezone database.
        printFullDateByJoda(dateTime, getLocale())
      }
      .onErrorResumeNext {
        // To handle https://fabric.io/skedgo/android/apps/com.buzzhives.android.tripplanner/issues/588311cd0aeb16625b5aeda1.
        when (it) {
          is IllegalArgumentException -> Observable.fromCallable {
            printFullDateByJava(dateTime.toDate(), getLocale())
          }
          else -> Observable.error(it)
        }
      }
      .subscribeOn(Schedulers.computation())
}