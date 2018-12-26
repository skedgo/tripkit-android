package skedgo.tripkit.routing

fun RealTimeVehicle.getAverageOccupancy(): Occupancy? =
    this.components?.let { components ->
      Occupancy.values()[components.flatten().map {
        it.getOccupancy()?.ordinal ?: 0
      }.average().toInt()]
    }

fun RealTimeVehicle.hasVehiclesOccupancy(): Boolean =
    components?.isNotEmpty() ?: false && components?.first()?.size ?: 0 > 1

fun RealTimeVehicle.hasSingleVehicleOccupancy(): Boolean =
    components?.isNotEmpty() ?: false && components?.first()?.size ?: 0 == 1