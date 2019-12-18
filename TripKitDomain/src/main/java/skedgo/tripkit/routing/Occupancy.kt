package skedgo.tripkit.routing

enum class Occupancy(val value: String) {
  Empty("EMPTY"),
  ManySeatsAvailable("MANY_SEATS_AVAILABLE"),
  FewSeatsAvailable("FEW_SEATS_AVAILABLE"),
  StandingRoomOnly("STANDING_ROOM_ONLY"),
  CrushedStandingRoomOnly("CRUSHED_STANDING_ROOM_ONLY"),
  Full("FULL"),
  NotAcceptingPassengers("NOT_ACCEPTING_PASSENGERS")
}

fun String?.toOccupancy(): Occupancy? = this?.let {
  Occupancy.values().first { it.value == this }
}