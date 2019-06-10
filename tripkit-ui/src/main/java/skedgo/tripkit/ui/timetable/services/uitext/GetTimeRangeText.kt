package skedgo.tripkit.ui.timetable.services.uitext

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import rx.Observable
import skedgo.tripkit.datetime.PrintTime
import javax.inject.Inject

open class GetTimeRangeText @Inject constructor(
    private val printTime: PrintTime
) {

  open fun execute(dateTimeZone: DateTimeZone, startTimeInSecs: Long, endTimeInSecs: Long): Observable<String> =
      printTime.execute(DateTime(startTimeInSecs * 1000, dateTimeZone))
          .zipWith(printTime.execute(DateTime(endTimeInSecs * 1000, dateTimeZone)))
          { start: String, end: String -> start to end }
          .map { (start, end) ->
            "$start - $end"
          }
}