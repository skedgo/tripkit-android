package com.skedgo.tripkit

import android.os.Parcelable

interface TransportModeFilter : Parcelable {
  /**
   * Used when routing to specify the desired transit mode options. The default implementation requests all
   * transport modes in a region. The actual requested routes will be a mix of routes involving the individual
   * modes as well as multi-modal routes combining two or more of the specified modes.
   *
   * @param allTransportModes is all transport modes supported by the routing region
   * @return Return mode IDs that will be included in routing requests
   */
  fun transportModes(allTransportModes: List<String>): List<String> {
    return allTransportModes
  }

  /**
   * Used when routing to decide which transport modes to avoid, such as a route where public transit is returned
   * from `transportModes` but you want to avoid ferries by specifying "pt_pub_ferry" as one to avoid. The default implementation
   * does not avoid any modes.
   *
   * @param allTransportModes is all transport modes supported by the routing region
   * @return Return mode IDs that will be avoided in routing requests
   */
  fun avoidTransportModes(allTransportModes: List<String>): List<String> {
    return listOf()
  }

}