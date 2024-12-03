package com.skedgo.tripkit.routing

/**
 * A decorator that puts additional query params
 * into the query map that is supplied into [A2bRoutingApi].
 * Note that you should only use this when
 * you really do know what you intend to do.
 */
interface ExtraQueryMapProvider {
    /**
     * Be careful that some entries of this map
     * may override some default entries of
     * the query map of [A2bRoutingApi].
     */
    fun call(): Map<String, Any>
}