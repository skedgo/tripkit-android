package skedgo.tripkit.timetable

import com.skedgo.android.common.model.RealtimeAlert
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TimetableEntry
import rx.Observable
import skedgo.tripkit.realtime.RealtimeAlertRepository
import javax.inject.Inject

open class FetchTimetable @Inject constructor(
    private val departuresRepository: DeparturesRepository,
    private val realtimeAlertRepository: RealtimeAlertRepository
) {

  open fun execute(embarkationStopCodes: List<String>,
                   region: Region,
                   startTimeInMillis: Long
  ): Observable<List<TimetableEntry>> =
      departuresRepository.getTimetableEntries(
          region.name!!,
          embarkationStopCodes,
          null,
          startTimeInMillis / 1000)
          .first()
          .map { response ->
            if (response == null) {
              error(RuntimeException("Failed to fetch timetable"))
            }

            // Save alerts
            if (!response.alerts.isNullOrEmpty()) {
              realtimeAlertRepository.addAlerts(response.alerts)
            }

            // Add real time alerts
            response.serviceList.forEach { service ->
              service.alertHashCodes?.let {
                realtimeAlertRepository.addAlertHashCodesForId(service.serviceTripId, it.toList())
              }
            }

            // set start stop from parent info
            response.serviceList.forEach { service ->
              service.startStop = response.parentInfo?.children?.find { it.code == service.stopCode }
            }

            response.serviceList
          }.map {
            it.forEach { timetable ->
              val savedAlerts = realtimeAlertRepository.getAlerts(timetable.serviceTripId)
              timetable.alerts = java.util.ArrayList<RealtimeAlert>()
              savedAlerts?.forEach { timetable.alerts.add(it) }
            }
            it
          }
}