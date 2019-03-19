package skedgo.tripkit.routing

fun RealTimeVehicle.getAverageOccupancy(): Occupancy? {
  return with(this.components) {
    if (this.isNullOrEmpty()) {
      null
    } else {
      this.flatten()
          .map {
            it.getOccupancy()!!.ordinal
          }
          .average()
          .toInt()
          .let {
            Occupancy.values()[it]
          }
    }
  }
}

fun RealTimeVehicle.hasVehiclesOccupancy(): Boolean {
  return with(components) { this != null && this.flatten().size > 1 }
}

fun RealTimeVehicle.hasSingleVehicleOccupancy(): Boolean {
  return with(components) {
    this != null && this.isNotEmpty() && this.size == 1 && this.first().size == 1
  }
}