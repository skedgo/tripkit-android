package skedgo.tripkit.ui.timetable.services.uitext

import com.skedgo.android.common.model.TimetableEntry
import org.joda.time.DateTimeZone
import rx.Observable
import javax.inject.Inject

open class GetServiceSubTitleText @Inject constructor(
    private val getTimeRangeText: GetTimeRangeText,
    private val getDirectionText: GetDirectionText
) {

  open fun execute(service: TimetableEntry, dateTimeZone: DateTimeZone): Observable<String> =
      service.startSecs().observe().filter { secs -> secs != 0L }
          .withLatestFrom(service.endSecs().observe())
          { startTimeInSecs, endTimeInSecs -> startTimeInSecs to endTimeInSecs }
          .flatMap { (startTimeInSecs, endTimeInSecs) ->
            when {
              service.isFrequencyBased -> getTimeRangeText.execute(dateTimeZone, startTimeInSecs, endTimeInSecs)
              else -> Observable.just(getDirectionText.execute(service))
            }
          }
}