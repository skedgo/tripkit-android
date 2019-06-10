package skedgo.tripkit.ui.timetable.services.uitext

import com.skedgo.android.common.model.TimetableEntry
import org.joda.time.DateTimeZone
import rx.Observable
import skedgo.tripkit.routing.RealTimeVehicle
import javax.inject.Inject

open class GetServiceTitleText @Inject constructor(
    private val getFrequencyText: GetFrequencyText,
    private val getA2BTime: GetA2BTime,
    private val getOrdinaryTime: GetOrdinaryTime
) {

  open fun execute(service: TimetableEntry, dateTimeZone: DateTimeZone, vehicle: RealTimeVehicle? = null): Observable<String> =
      service.startSecs().observe().filter { secs -> secs != 0L }
          .withLatestFrom(service.endSecs().observe())
          { startTimeInSecs, endTimeInSecs -> startTimeInSecs to endTimeInSecs }
          .flatMap { (startTimeInSecs, endTimeInSecs) ->
            when {
              service.isFrequencyBased -> Observable.just(getFrequencyText.execute(service))
              endTimeInSecs != 0L -> getA2BTime.execute(dateTimeZone, service, startTimeInSecs, endTimeInSecs)
              else -> getOrdinaryTime.execute(dateTimeZone, service, vehicle)
            }
          }
}