package com.skedgo.android.tripkit.waypoints

import android.content.res.Resources
import com.google.gson.Gson
import com.skedgo.android.common.model.TripGroup
import rx.Observable
import rx.plugins.RxJavaHooks.onError
import javax.inject.Inject

class WaypointService
@Inject constructor(private val api: WaypointApi, private val resources: Resources, private val gson: Gson) {

    fun fetchChangedTripAsync(config: ConfigurationParams, segments: List<WaypointSegmentAdapter>): Observable<List<TripGroup>> {

        val body = WaypointBody(config, segments)
        return api.getChangedTrip(body)
                .map { routingResponse ->
                    routingResponse.processRawData(resources, gson)
                    val tripGroups = routingResponse.tripGroupList
                    if (tripGroups!![0].trips!!.isEmpty()) {
                        onError(RuntimeException("No groups found"))
                    }
                    routingResponse.tripGroupList
                }

    }

}

class WaypointBody(val config: ConfigurationParams,
                   val segments: List<WaypointSegmentAdapter>)