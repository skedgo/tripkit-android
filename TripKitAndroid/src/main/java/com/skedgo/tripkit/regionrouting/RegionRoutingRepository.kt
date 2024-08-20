package com.skedgo.tripkit.regionrouting

import com.skedgo.tripkit.common.model.Location
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.regionrouting.data.GetRegionRouteRequest
import com.skedgo.tripkit.regionrouting.data.GetRouteDetailsRequest
import com.skedgo.tripkit.regionrouting.data.RegionRoute
import com.skedgo.tripkit.regionrouting.data.RouteDetails
import io.reactivex.Observable
import io.reactivex.Single

interface RegionRoutingRepository {

    fun getRegionRoutes(
        region: String,
        query: String? = null,
        modes: List<String> = emptyList(),
        operatorId: String? = null
    ): Single<List<RegionRoute>>

    fun getRoutes(
        query: String,
        location: Location?
    ): Observable<List<RegionRoute>>

    fun getRoutes(
        regionName: String,
        query: String
    ): Single<List<RegionRoute>>

    fun getRegionRouteInfo(
        region: String,
        operatorId: String,
        routeID: Int
    ): Single<RouteDetails>

    class RegionRoutingRepositoryImpl(
        private val regionRoutingApi: RegionRoutingApi,
        private val regionService: RegionService
    ) : RegionRoutingRepository {
        override fun getRegionRoutes(
            region: String,
            query: String?,
            modes: List<String>,
            operatorId: String?
        ): Single<List<RegionRoute>> {
            return regionRoutingApi.getRegionRoutes(
                GetRegionRouteRequest(
                    region = region,
                    query = query,
                    modes = modes,
                    operatorId = operatorId
                )
            )
        }

        override fun getRoutes(regionName: String, query: String): Single<List<RegionRoute>> {
            val request = GetRegionRouteRequest(
                region = regionName,
                query = query
            )
            return regionRoutingApi.getRegionRoutes(request)
        }

        override fun getRoutes(query: String, location: Location?): Observable<List<RegionRoute>> {
            return (
                if (location != null) {
                    regionService.getRegionByLocationAsync(location)
                } else {
                    regionService.getRegionsAsync().map { it.first() }
                }
                ).flatMapSingle { region ->
                    val request = GetRegionRouteRequest(
                        region = region.name ?: "",
                        query = query
                    )
                    regionRoutingApi.getRegionRoutes(request)
                }
        }

        override fun getRegionRouteInfo(
            region: String,
            operatorId: String,
            routeID: Int
        ): Single<RouteDetails> {
            val request =
                GetRouteDetailsRequest(region, operatorId, routeID)

            return regionRoutingApi.getRegionRouteInfo(request)
        }
    }

}