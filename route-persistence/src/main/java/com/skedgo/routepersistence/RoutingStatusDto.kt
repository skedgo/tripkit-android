package com.skedgo.routepersistence

import skedgo.tripkit.routingstatus.Status

fun Status.toDto(): String {
  return when (this) {
    is Status.Completed -> "Completed"
    is Status.Error -> "Error"
    is Status.InProgress -> "InProgress"
  }
}

fun String.toStatus(): Status {
  return when (this) {
    "Completed" -> Status.Completed()
    "Error" -> Status.Error()
    "InProgress" -> Status.InProgress()
    else -> error("Unknown status: ${this}")
  }
}
