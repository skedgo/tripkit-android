package com.skedgo.tripkit.data.datetime

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.skedgo.tripkit.datetime.PrintFullDate
import java.text.DateFormat
import java.util.Locale;
import java.util.Date;

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
  override fun execute(dateTime: DateTime): Flowable<String>
      = Flowable
      .fromCallable {
        // Still relies on Joda because of its up-to-date timezone database.
        printFullDateByJoda(dateTime, getLocale())
      }
      .onErrorResumeNext {throwable: Throwable ->
          // To handle https://fabric.io/skedgo/android/apps/com.buzzhives.android.tripplanner/issues/588311cd0aeb16625b5aeda1.
          when (throwable) {
              is IllegalArgumentException -> Flowable.fromCallable {
                  printFullDateByJava(dateTime.toDate(), getLocale())
              }
              else -> Flowable.error(throwable)
          }
      }
          .subscribeOn(Schedulers.computation())
}