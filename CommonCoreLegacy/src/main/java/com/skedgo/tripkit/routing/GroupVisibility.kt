package com.skedgo.tripkit.routing

enum class GroupVisibility(
    /**
     * To be sortable.
     */
    @JvmField val value: Int
) {
    FULL(1), COMPACT(0)
}
