package skedgo.tripkit.ui.timetable.services.uitext

import android.content.Context
import com.skedgo.android.common.model.TimetableEntry
import org.joda.time.DateTimeZone
import rx.Observable
import skedgo.tripkit.ui.R
import javax.inject.Inject

open class GetA2BTime @Inject constructor(
    private val context: Context,
    private val getTimeRangeText: GetTimeRangeText
) {
  open fun execute(dateTimeZone: DateTimeZone, service: TimetableEntry, startTimeInSecs: Long, endTimeInSecs: Long): Observable<String> =
      getTimeRangeText.execute(dateTimeZone, startTimeInSecs, endTimeInSecs)
          .map {
            "${when {
              service.serviceNumber.isNotEmpty() -> service.serviceNumber
              service.startStop?.type != null -> service.startStop.type.toString().capitalize()
              else -> context.getString(R.string.service)
            }}: $it"
          }
}