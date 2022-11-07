package com.skedgo.tripkit.regionrouting

import io.reactivex.Single

interface GetRegionRoutingService {

    fun getRegionRoutes(
            region: String,
            query: String? = null,
            modes: List<String> = emptyList(),
            operatorId: String? = null
    ): Single<GetRegionRouteResponse>

    class GetRegionRoutingServiceImpl(private val regionRoutingApi: RegionRoutingApi) : GetRegionRoutingService {
        override fun getRegionRoutes(
                region: String,
                query: String?,
                modes: List<String>,
                operatorId: String?
        ): Single<GetRegionRouteResponse> {
            return regionRoutingApi.getRegionRoutes(
                    GetRegionRouteRequest(
                            region = region,
                            query = query,
                            modes = modes,
                            operatorId = operatorId
                    )
            )
        }
    }

}