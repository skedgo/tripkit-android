package com.skedgo.tripkit.data.locations

import io.reactivex.Observable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Coordinates calls to `POST /satapp/locations.json` made via [StopsFetcher].
 *
 * Two responsibilities:
 *
 * 1. Recently-fetched tracking — a per-cell, in-memory timestamp of when we last successfully
 *    completed a fetch for that cell/region/level combination. While the timestamp is within
 *    [ttlMs] the cell is treated as "fresh" and excluded from the next request, so a small
 *    camera pan inside an already-loaded area does not re-hit the API. This works together
 *    with the persisted `scheduled_stops_download_history` table, which survives process
 *    death but no longer skips empty cells.
 *
 * 2. In-flight de-duplication — when multiple paths (viewport pipeline + prefetch + UI
 *    callback) trigger the same request key concurrently, only the first network call goes
 *    out; the rest share its result. This is critical because [com.skedgo.tripkit.ui.map.home.MapViewModel]
 *    routinely fires a main fetch and a buffered prefetch within milliseconds of each other.
 *
 * Designed to be a process-scoped singleton (provided via Dagger).
 */
class LocationsFetchCoordinator(
    private val ttlMs: Long = DEFAULT_TTL_MS,
    private val clock: () -> Long = { System.currentTimeMillis() }
) {

    private val recentlyFetched = ConcurrentHashMap<String, Long>()
    private val inFlight = ConcurrentHashMap<String, Observable<List<LocationsResponse.Group>>>()

    /**
     * Returns the subset of [cellIds] that were NOT successfully fetched within the TTL
     * window. Callers should only request these cell ids from the network.
     */
    fun filterStaleCellIds(
        cellIds: Collection<String>,
        regionName: String,
        level: Int
    ): List<String> {
        if (cellIds.isEmpty()) return emptyList()
        val cutoff = clock() - ttlMs
        return cellIds.filter { cellId ->
            val ts = recentlyFetched[trackerKey(cellId, regionName, level)]
            ts == null || ts < cutoff
        }
    }

    /**
     * Marks [cellIds] as successfully fetched at `now`. Called after a response is received,
     * regardless of whether the cells came back with data — empty responses must be recorded
     * too so sparse areas (e.g. AU_NT_Darwin grid cells with no stops) stop being re-fetched
     * on every viewport change.
     */
    fun recordFetched(
        cellIds: Collection<String>,
        regionName: String,
        level: Int
    ) {
        if (cellIds.isEmpty()) return
        val now = clock()
        for (cellId in cellIds) {
            recentlyFetched[trackerKey(cellId, regionName, level)] = now
        }
    }

    /**
     * Runs [block] under an in-flight guard keyed by [key]. If another subscriber is already
     * waiting on the same key, they share the same underlying network call.
     *
     * The shared observable is removed from the map on terminate so the next request after
     * completion re-evaluates cache freshness instead of replaying a stale result.
     */
    fun shareInFlight(
        key: String,
        block: () -> Observable<List<LocationsResponse.Group>>
    ): Observable<List<LocationsResponse.Group>> {
        inFlight[key]?.let { return it }
        val shared = block()
            .doFinally { inFlight.remove(key) }
            .replay(1)
            .refCount()
        val existing = inFlight.putIfAbsent(key, shared)
        return existing ?: shared
    }

    fun hasInFlight(key: String): Boolean = inFlight.containsKey(key)

    private fun trackerKey(cellId: String, regionName: String, level: Int): String =
        "$regionName|$level|$cellId"

    companion object {
        /**
         * 10 minutes: conservative balance between API cost and freshness for relatively
         * static POI data (stops, car parks, bike pods, facilities). Live transit info is
         * fetched via a different endpoint, so this TTL only affects marker visibility.
         */
        val DEFAULT_TTL_MS: Long = TimeUnit.MINUTES.toMillis(10)
    }
}
