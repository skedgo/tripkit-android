package skedgo.tripkit.routing

fun TripGroup.containsMode(modeId: String): Boolean =
    trips?.any {
      it.segments.any {
        it.transportModeId == modeId
      }
    } ?: false

fun TripGroup.containsAnyMode(modeIds: List<String>): Boolean =
    modeIds.any { containsMode(it) }

fun TripGroup.getTrip(tripId: Long): Trip? = trips?.find { it.id == tripId }
