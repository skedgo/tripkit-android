package com.skedgo.tripkit

import android.util.Log
import com.skedgo.tripkit.data.tsp.RegionInfo
import skedgo.tripkit.routing.ModeInfo

interface TransitModeFilter {
  /**
   * @param regionInfo is the regionInfo of the Region that will be used for routing
   * @return Return modes that will be included in routing requests
   */
  fun filterTransitModes(regionInfo: RegionInfo): List<ModeInfo> {
    return regionInfo.transitModes()!!
  }
}
