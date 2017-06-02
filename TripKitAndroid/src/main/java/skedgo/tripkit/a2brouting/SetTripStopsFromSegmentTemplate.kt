package skedgo.tripkit.a2brouting

import android.util.SparseArray
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.skedgo.android.common.model.ServiceStop
import skedgo.tripkit.routing.RoutingResponse
import skedgo.tripkit.routing.RoutingResponse.*
import skedgo.tripkit.routing.TripSegment
import skedgo.tripkit.routing.endDateTime
import skedgo.tripkit.routing.toSeconds
import javax.inject.Inject

open class SetTripStopsFromSegmentTemplate @Inject constructor(private val gson: Gson) {

  open fun execute(routingResponse: RoutingResponse) {
    val segmentTemplateMap = routingResponse.createSegmentTemplateMap()

    routingResponse.tripGroupList?.forEach {
      it.trips?.forEach { trip ->
        trip.segments?.filter { it.hasTimeTable() }?.forEach { segment ->
          setStopsFromSegmentTemplate(segmentTemplateMap, segment, trip.endDateTime.toSeconds())
        }
      }
    }
  }

  fun setStopsFromSegmentTemplate(segmentTemplateMap: SparseArray<JsonObject>,
                                          segment: TripSegment,
                                          tripEndDateTime: Long
  ) {
    val segmentTemplateJson = segmentTemplateMap.get(segment.templateHashCode.toInt())
    if (segmentTemplateJson.has(NODE_SHAPES)) {
      segmentTemplateJson.getAsJsonArray(NODE_SHAPES)
          .filter {
            it.asJsonObject.has(NODE_STOPS) &&
                it.asJsonObject.has(NODE_TRAVELLED) &&
                it.asJsonObject.get(NODE_TRAVELLED).asBoolean
          }.first().let {
        val stopsJsonArray = it.asJsonObject.get(NODE_STOPS).asJsonArray
        val stops = mutableListOf<ServiceStop>()
        stopsJsonArray?.forEach {
          val stop = gson.fromJson(it, ServiceStop::class.java)
          stop.arrivalTime = stop.relativeArrival + tripEndDateTime
          stops.add(stop)
        }
        segment.stops().put(stops)
      }
    }
  }
}