package com.skedgo.tripkit.regionrouting

import io.reactivex.Single
import retrofit2.http.POST

interface RegionRoutingApi {
    @POST("info/routes.json")
    fun getRegionRoutes(request: GetRegionRouteRequest): Single<GetRegionRouteResponse>
}