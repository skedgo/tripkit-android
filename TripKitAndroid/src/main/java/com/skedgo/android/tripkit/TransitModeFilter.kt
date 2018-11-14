package com.skedgo.android.tripkit

interface TransitModeFilter {
  /**
   * @param allTransitModes is all transport modes supported by the routing region
   * @return Return modes that will be included in routing requests
   */
  fun filterTransitModes(allTransitModes: List<String>): List<String> {
    return allTransitModes
  }
}