package com.skedgo.android.common.model

enum class Availability(val value: String) {
  Available("AVAILABLE"),
  MissedPrebookingWindow("MISSED_PREBOOKING_WINDOW"),
  Cancelled("CANCELLED");

  companion object {
    @JvmStatic fun fromString(value: String?): Availability? {
      return values().firstOrNull { it.value == value }
    }
  }
}