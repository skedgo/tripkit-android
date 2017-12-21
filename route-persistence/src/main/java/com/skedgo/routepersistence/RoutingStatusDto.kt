package com.skedgo.routepersistence

import skedgo.tripkit.routingstatus.Status

fun Status.toDto(): RoutingStatusDto {
  return when (this) {
    is Status.Completed -> RoutingStatusDto("Completed", "")
    is Status.Error -> RoutingStatusDto("Error", this.message)
    is Status.InProgress -> RoutingStatusDto("InProgress", "")
  }
}

fun RoutingStatusDto.toStatus(): Status {
  return when (this.first) {
    "Completed" -> Status.Completed()
    "Error" -> Status.Error(this.second)
    "InProgress" -> Status.InProgress()
    else -> error("Unknown status: ${this}")
  }
}
typealias RoutingStatusDto = Pair<String, String>