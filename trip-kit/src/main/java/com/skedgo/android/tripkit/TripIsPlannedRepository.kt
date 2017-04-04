package com.skedgo.android.tripkit

import skedgo.tripkit.analytics.Choice

interface TripIsPlannedRepository {

  fun markPlannedTrip(plannedUrl: String)
  fun markPlannedTrip(plannedUrl: String, choiceSet : List<Choice>)
}