package skedgo.tripkit.timetable

import com.skedgo.android.common.StyleManager
import com.skedgo.android.common.model.DeparturesResponse

fun DeparturesResponse.postProcess(embarkationStops: List<String>,
                                   disembarkationStops: List<String>?): DeparturesResponse =
    this.apply {
      // Must proceed this so that services can have theirs proper stop code
      processEmbarkationStopList()

      if (disembarkationStops?.isNotEmpty() == true &&
          embarkationStops.isNotEmpty() &&
          serviceList.isNotEmpty()) {
        // We've just fetched A2B timetable.
        // So, we need to update service's pairIdentifier so that
        // next time, we can query services for a given pair of A2B.
        val startStopCode = embarkationStops[0]
        val endStopCode = disembarkationStops[0]
        for (service in serviceList) {
          service.pairIdentifier = String.format(
              StyleManager.FORMAT_PAIR_IDENTIFIER,
              startStopCode,
              endStopCode
          )

        }
      } // Else, we've fetched timetable for a stop

      // Save original service time (for real time services)
      if (serviceList.isNotEmpty()) {
        for (service in serviceList) {
          service?.serviceTime = service.startTimeInSecs
        }
      }
    }
