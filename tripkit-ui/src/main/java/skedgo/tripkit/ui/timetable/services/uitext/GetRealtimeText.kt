package skedgo.tripkit.ui.timetable.services.uitext

import android.content.Context
import com.skedgo.android.common.model.TimetableEntry
import com.skedgo.android.common.util.TimeUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import rx.Observable
import skedgo.tripkit.datetime.PrintTime
import skedgo.tripkit.routing.RealTimeVehicle
import skedgo.tripkit.ui.R
import skedgo.tripkit.ui.timetable.services.realTimeDeparture
import skedgo.tripkit.ui.timetable.services.realTimeNotAvailable
import javax.inject.Inject

open class GetRealtimeText @Inject constructor(
    private val context: Context,
    private val printTime: PrintTime
) {

  open fun execute(dateTimeZone: DateTimeZone, service: TimetableEntry, vehicle: RealTimeVehicle? = null): Observable<String> =
      when {
        realTimeNotAvailable(service, vehicle) -> Observable.just(context.getString(R.string.no_realtime_available))
        else -> {
          printTime.execute(DateTime(service.serviceTime * 1000, dateTimeZone))
              .map {
                val realtimeDeparture = realTimeDeparture(service, vehicle)
                val timeDiff = service.serviceTime - realtimeDeparture

                when {
                  Math.abs(timeDiff.toInt()) < 60 -> context.getString(R.string.on_time)
                  realtimeDeparture < service.serviceTime ->
                    String.format(context.getString(R.string._pattern_early__start_parent_pattern_service_end_parent),
                        TimeUtils.getDurationInHoursMins(Math.abs(timeDiff.toInt())), it)
                  else -> String.format(context.getString(R.string._pattern_late__start_parent_pattern_service_end_parent),
                      TimeUtils.getDurationInHoursMins(Math.abs(timeDiff.toInt())), it)
                }
              }
        }
      }
}