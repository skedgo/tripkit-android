package com.skedgo.tripkit

import android.os.Parcelable

interface TransportModeFilter : Parcelable {
    /**
     * Used when routing to specify the desired transit modes. The default implementation supports all modes available
     * in a region.
     *
     * @param mode is a transport mode
     * @return Return true if it should be used for routing, false if not.
     */
    fun useTransportMode(mode: String): Boolean {
        return true
    }

    /**
     * Used when routing to decide which transport modes to avoid, such as a route where public transit is returned
     * from `transportModes` but you want to avoid ferries by specifying "pt_pub_ferry" as one to avoid. The default implementation
     * does not avoid any modes.
     *
     * @param mode is a transport mode
     * @return Return true if it should be avoided in routing, false if not.
     */

    fun avoidTransportMode(mode: String): Boolean {
        return false
    }

    fun getFilteredMode(originalModes: List<String>): List<String>
}