package com.skedgo.android.common.model

enum class Occupancy(val string: String) {
  Empty("EMPTY"),
  ManySeatsAvailable("MANY_SEATS_AVAILABLE"),
  FewSeatsAvailable("FEW_SEATS_AVAILABLE"),
  StandingRoomOnly("STANDING_ROOM_ONLY"),
  CrushedStandingRoomOnly("CRUSHED_STANDING_ROOM_ONLY"),
  Full("FULL"),
  NotAcceptingPassengers("NOT_ACCEPTING_PASSENGERS")
}