package com.skedgo.tripkit

interface TransportModeFilter {
  /**
   * @param allTransportModes is all transport modes supported by the routing region
   * @return Return modes that will be included in routing requests
   */
  fun filterModes(allTransportModes: List<String>): List<String> {
    return allTransportModes
  }
}