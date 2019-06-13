package skedgo.tripkit.realtime

import com.skedgo.android.common.agenda.IRealTimeElement
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.util.TimeUtils.InMillis
import rx.Observable
import skedgo.tripkit.routing.RealTimeVehicle
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val UPDATE_INTERVAL = InMillis.SECOND * 15

open class RealTimeChoreographer @Inject constructor(
    private val realTimeRepository: RealTimeRepository
) {

  open fun getRealTimeResults(region: Region, elements: List<IRealTimeElement>): Observable<List<RealTimeVehicle>> {
    return realTimeRepository.getUpdates(region.name!!, elements)
        .repeatWhen { observable -> observable.delay(UPDATE_INTERVAL, TimeUnit.MILLISECONDS) }
  }

  open fun getRealTimeResultsFromCleanElements(
      region: Region, elements: List<IRealTimeElement?>): Observable<List<RealTimeVehicle>> {
    return getRealTimeResults(region, cleanSegments(elements))
  }

  private fun cleanSegments(elements: List<IRealTimeElement?>?): List<IRealTimeElement> {
    return elements.orEmpty()
        .filter { it != null }
        .map { it!! }
        .distinctBy { Triple(it.serviceTripId, it.startStopCode, it.endStopCode) }
  }
}