package com.skedgo.tripkit.regionrouting

import com.skedgo.tripkit.regionrouting.data.GetRegionRouteRequest
import com.skedgo.tripkit.regionrouting.data.GetRouteDetailsRequest
import com.skedgo.tripkit.regionrouting.data.RegionRoute
import com.skedgo.tripkit.regionrouting.data.RouteDetails
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface RegionRoutingApi {
    @POST("info/routes.json")
    fun getRegionRoutes(@Body request: GetRegionRouteRequest): Single<List<RegionRoute>>

    @POST("info/routeInfo.json")
    fun getRegionRouteInfo(@Body request: GetRouteDetailsRequest): Single<RouteDetails>
}