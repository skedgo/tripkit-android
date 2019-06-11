package skedgo.tripkit.realtime

import com.skedgo.android.common.model.RealtimeAlert
import skedgo.tripkit.routing.RealTimeVehicle

fun LatestServiceResponse.toRealTimeVehicle() =
    (this.realtimeVehicle() ?: RealTimeVehicle())
        .apply {
          alerts = ArrayList<RealtimeAlert>(alerts().orEmpty())
          serviceTripId = serviceTripID()
          startStopCode = startStopCode()
          endStopCode = endStopCode()
          arriveAtStartStopTime = startTime() ?: 0
          arriveAtEndStopTime = endTime() ?: 0
        }