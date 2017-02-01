package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.RoutingResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface WaypointApi {

  @POST("waypoint.json") Observable<RoutingResponse> getChangedTrip(@Body WaypointBody body);
}
