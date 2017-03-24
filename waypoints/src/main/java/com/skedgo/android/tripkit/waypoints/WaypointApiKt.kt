package com.skedgo.android.tripkit.waypoints

import com.skedgo.android.common.model.RoutingResponse

import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface WaypointApi {

    @POST("waypoint.json") fun getChangedTrip(@Body body: WaypointBody): Observable<RoutingResponse>
}


