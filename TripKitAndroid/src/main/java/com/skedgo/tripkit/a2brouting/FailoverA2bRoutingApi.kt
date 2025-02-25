package com.skedgo.tripkit.a2brouting

import android.content.res.Resources
import com.google.gson.Gson
import com.skedgo.TripKit
import com.skedgo.rxtry.printThrowableStackTrace
import com.skedgo.tripkit.RoutingUserError
import com.skedgo.tripkit.extensions.buildUrlWithQueryParams
import com.skedgo.tripkit.routing.RoutingResponse
import com.skedgo.tripkit.routing.RoutingResponse.Companion.ERROR_CODE_NO_FROM_LOCATION
import com.skedgo.tripkit.routing.TripGroup
import io.reactivex.Maybe
import io.reactivex.Observable

/**
 * A wrapper of [A2bRoutingApi] that requests `routing.json`
 * on multiple servers w/ failover.
 */
class FailoverA2bRoutingApi(
    private val resources: Resources,
    private val gson: Gson,
    private val a2bRoutingApi: A2bRoutingApi
) {
    private val selectBestDisplayTrip = SelectBestDisplayTrip()
    private val fillIdentifiers = FillIdentifiers()

    /**
     * Fetches routes on multiple base URLs serially.
     * If it fails on one URL, it'll failover to the next URL.
     *
     * @param baseUrls Can be obtained by Region.getURLs().
     */
    fun fetchRoutesAsync(
        baseUrls: List<String>,
        modes: List<String>,
        excludedTransitModes: List<String>,
        excludeStops: List<String>,
        options: Map<String, Any>
    ): Observable<List<TripGroup>> {
        return Observable.fromIterable(baseUrls)
            .map { baseUrl ->
                baseUrl.buildUrlWithQueryParams(
                    modes,
                    excludedTransitModes,
                    excludeStops,
                    options,
                    TripKit.getInstance().configs().isGroupedDrt
                )
            }
            .concatMap { url ->
                fetchRoutesPerUrlAsync(url, modes, excludedTransitModes, excludeStops, options)
                    .map { response ->
                        val tripGroups = response.tripGroupList
                        if (!tripGroups.isNullOrEmpty()) {
                            tripGroups.forEach { group ->
                                group.fullUrl = url
                                selectBestDisplayTrip.apply(group)
                            }
                        }
                        response
                    }
            }
            .first(RoutingResponse())
            .map { response ->
                response.processRawData(resources, gson)
                response.tripGroupList
            }
            .filter { it.isNotEmpty() }
            .map(fillIdentifiers)
            .map { groups ->
                groups.forEach { group -> selectBestDisplayTrip.apply(group) }
                groups
            }
            .onErrorResumeNext { error: Throwable ->
                error.printThrowableStackTrace()
                if (error is RoutingUserError) Maybe.error(error) else Maybe.empty()
            }
            .toObservable()
    }

    private fun fetchRoutesPerUrlAsync(
        url: String,
        modes: List<String>,
        excludedTransitModes: List<String>,
        excludeStops: List<String>,
        options: Map<String, Any>
    ): Observable<RoutingResponse> {
        return a2bRoutingApi.execute(url, modes, excludedTransitModes, excludeStops, options)
            .filter { response -> !(response.errorMessage != null && !response.hasError()) }
            .onErrorResumeNext(Observable.empty())
            .flatMap { response ->
                if (response.errorMessage != null) {
                    if (response.errorCode == ERROR_CODE_NO_FROM_LOCATION) {
                        Observable.empty()
                    } else {
                        Observable.error(RoutingUserError(response.errorMessage.orEmpty()))
                    }
                } else {
                    Observable.just(response)
                }
            }
    }
}