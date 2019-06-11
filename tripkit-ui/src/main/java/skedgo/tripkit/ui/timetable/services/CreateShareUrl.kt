package skedgo.tripkit.ui.timetable.services

import com.skedgo.android.common.model.TimetableEntry
import com.skedgo.android.common.model.RealTimeStatus
import com.skedgo.android.common.model.ScheduledStop
import com.skedgo.android.tripkit.RegionService
import rx.Observable
import javax.inject.Inject

open class CreateShareUrl @Inject constructor(
    private val regionService: RegionService,
    private val timeUtilsWrapper: TimeUtilsWrapper
) {

  open fun execute(stop: ScheduledStop, services: List<TimetableEntry>): Observable<String> =

      regionService.getRegionByLocationAsync(stop)
          .map { region ->

            val rows = StringBuilder()
            val limit = 10
            var gotARealTime = false

            services.take(limit).forEach {
              val isRealTime = RealTimeStatus.IS_REAL_TIME == it.realTimeStatus
              gotARealTime = gotARealTime or isRealTime
              rows.append("${it.serviceNumber} (${it.serviceName}) ${timeUtilsWrapper.getTimeInDay(it.startTimeInSecs)}")
              if (isRealTime) {
                rows.append("*")
              }
              rows.append("\n")
            }

            if (gotARealTime) {
              rows.append("* real-time")
            }

            rows.append("\nhttp://tripgo.me/stop/${region.name}/${stop.code}")

            rows.toString()
          }
}