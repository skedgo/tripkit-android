package skedgo.tripkit.samples.a2brouting

import com.skedgo.tripkit.routing.TripGroup

object TripGroupRepository {

  val tripGroups = ArrayList<TripGroup>()

  fun putAll(newTripGroups: List<TripGroup>) = tripGroups.addAll(newTripGroups)

  fun getTripGroup(tripGroupUUID: String) = tripGroups.find { it.uuid() == tripGroupUUID }

  fun getTrip(tripId: Long) = tripGroups
      .filter { it.trips != null }
      .flatMap({ it.trips!!.asIterable() })
      .find { it.id == tripId }
}