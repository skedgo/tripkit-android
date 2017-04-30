package skedgo.tripkit.datetime

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import rx.Observable
import rx.schedulers.Schedulers
import java.text.DateFormat
import java.util.*

internal class PrintFullDateImpl constructor(
    private val getJodaFullDateFormatter: () -> DateTimeFormatter = {
      DateTimeFormat.fullDate()
    },
    private val getJavaFullDateFormat: () -> DateFormat = {
      DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault())
    }
) : PrintFullDate {
  override fun execute(dateTime: DateTime): Observable<String>
      = Observable
      .fromCallable {
        getJodaFullDateFormatter().print(dateTime)
      }
      .onErrorResumeNext {
        // To handle https://fabric.io/skedgo/android/apps/com.buzzhives.android.tripplanner/issues/588311cd0aeb16625b5aeda1.
        when (it) {
          is IllegalArgumentException -> Observable.fromCallable {
            getJavaFullDateFormat().format(dateTime.toDate())
          }
          else -> Observable.error(it)
        }
      }
      .subscribeOn(Schedulers.computation())
}