package skedgo.tripkit.routing

fun TripGroup.containsMode(modeId: String): Boolean =
    trips?.any {
      it.segments
          .filter { it.transportModeId != null }
          .map { it.transportModeId!! }
          .any {
            it.isChildModeOf(modeId)
          }
    } ?: false


internal fun String.isChildModeOf(parentMode: String): Boolean {
  return this.startsWith(parentMode) && this.substringAfter(parentMode).contains("-").not()
}

fun TripGroup.containsAnyMode(modeIds: List<String>): Boolean =
    modeIds.any { containsMode(it) }

fun TripGroup.getTrip(tripId: Long): Trip? = trips?.find { it.id == tripId }