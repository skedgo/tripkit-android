package skedgo.tripkit.ui.timetable.services

import com.skedgo.android.common.model.TimetableEntry
import com.skedgo.android.common.model.RealTimeStatus
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import skedgo.tripkit.routing.RealTimeVehicle
import java.util.concurrent.TimeUnit

fun realTimeDeparture(service: TimetableEntry, vehicle: RealTimeVehicle? = null): Long {
  val time = vehicle?.arriveAtStartStopTime ?: service.realTimeDeparture.toLong()

  return if (time == -1L || time == 0L) service.startTimeInSecs
  else time
}


fun realTimeNotAvailable(service: TimetableEntry, vehicle: RealTimeVehicle?): Boolean =
    (service.realTimeStatus != RealTimeStatus.IS_REAL_TIME && service.realTimeStatus != RealTimeStatus.CAPABLE || vehicle == null && service.realTimeDeparture == -1)
        || realTimeDeparture(service, vehicle) == 0L
        || realTimeDeparture(service, vehicle) == -1L

fun TimetableEntry.getTimeLeftToDepartInterval(period: Long, timeUnit: TimeUnit, realTimeVehicle: RealTimeVehicle? = null): Observable<Long> {
  return Observable.interval(0, period, timeUnit, AndroidSchedulers.mainThread())
      .map {
        realTimeDeparture(this, realTimeVehicle)
      }.map { secs ->
        val secsToDepart = TimeUnit.SECONDS.toMillis(secs!!) - System.currentTimeMillis()
        TimeUnit.MILLISECONDS.toMinutes(secsToDepart)
      }
}