package skedgo.tripkit.ui.timetable.services.uitext

import android.content.Context
import com.skedgo.android.common.model.TimetableEntry
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import rx.Observable
import skedgo.tripkit.datetime.PrintTime
import skedgo.tripkit.routing.RealTimeVehicle
import skedgo.tripkit.ui.R
import skedgo.tripkit.ui.timetable.services.realTimeDeparture
import javax.inject.Inject

open class GetOrdinaryTime @Inject constructor(
    private val context: Context,
    private val printTime: PrintTime
) {
  open fun execute(dateTimeZone: DateTimeZone, service: TimetableEntry, vehicle: RealTimeVehicle? = null): Observable<String> =
      printTime.execute(DateTime(realTimeDeparture(service, vehicle) * 1000, dateTimeZone))
          .map {
            val firstString = when {
              service.serviceNumber?.isNotEmpty() ?: false -> service.serviceNumber
              service.startStop?.type != null -> service.startStop.type.toString().capitalize()
              else -> context.getString(R.string.service)
            }
            context.getString(R.string._pattern_at__pattern, firstString, it)
          }
}