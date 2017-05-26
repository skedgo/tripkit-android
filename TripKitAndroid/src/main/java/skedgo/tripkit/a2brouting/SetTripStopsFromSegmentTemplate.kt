package skedgo.tripkit.a2brouting

import com.google.gson.Gson
import com.skedgo.android.common.model.ServiceStop
import skedgo.tripkit.routing.RoutingResponse
import skedgo.tripkit.routing.RoutingResponse.*
import skedgo.tripkit.routing.endDateTime
import skedgo.tripkit.routing.toSeconds
import javax.inject.Inject

class SetTripStopsFromSegmentTemplate @Inject constructor(private val gson: Gson) {

  fun execute(routingResponse: RoutingResponse) {
    val segmentTemplateMap = routingResponse.createSegmentTemplateMap()

    routingResponse.tripGroupList?.forEach {
      it.trips?.forEach { trip ->
        trip.segments?.forEach { segment ->
          if (segment.hasTimeTable()) {
            val segmentTemplateJson = segmentTemplateMap.get(segment.templateHashCode.toInt())
            if (segmentTemplateJson.has(NODE_SHAPES)) {
              segmentTemplateJson.getAsJsonArray(NODE_SHAPES)
                  .filter {
                    it.asJsonObject.has(NODE_TRAVELLED) &&
                        it.asJsonObject.get(NODE_TRAVELLED).asBoolean
                  }.first().let {
                if ( it.asJsonObject.has(NODE_STOPS)) {
                  val stopsJsonArray = it.asJsonObject.get(NODE_STOPS).asJsonArray
                  val stops = mutableListOf<ServiceStop>()
                  stopsJsonArray?.forEach {
                    val stop = gson.fromJson(it, ServiceStop::class.java)
                    stop.arrivalTime = stop.relativeArrival + trip.endDateTime.toSeconds()
                    stops.add(stop)
                  }
                  segment.stops().put(stops)
                }
              }
            }
          }
        }
      }
    }
  }
}