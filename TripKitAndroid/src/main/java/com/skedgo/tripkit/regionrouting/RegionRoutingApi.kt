package com.skedgo.tripkit.regionrouting

import com.skedgo.tripkit.regionrouting.data.GetRegionRouteRequest
import com.skedgo.tripkit.regionrouting.data.GetRouteDetailsRequest
import com.skedgo.tripkit.regionrouting.data.RegionRoute
import com.skedgo.tripkit.regionrouting.data.RouteDetails
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface RegionRoutingApi {
    @POST("info/routes.json")
    fun getRegionRoutes(@Body request: GetRegionRouteRequest): Single<List<RegionRoute>>

    @POST
    fun getRegionRoutes(@Url customUrl: String, @Body request: GetRegionRouteRequest): Single<List<RegionRoute>>

    @POST("info/routeInfo.json")
    fun getRegionRouteInfo(@Body request: GetRouteDetailsRequest): Single<RouteDetails>
}