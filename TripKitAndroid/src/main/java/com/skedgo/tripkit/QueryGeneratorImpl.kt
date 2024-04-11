package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.LOCATION_CLASS_SCHOOL
import com.skedgo.tripkit.common.model.Query
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.data.regions.RegionService
import io.reactivex.Observable

internal class QueryGeneratorImpl(private val regionService: RegionService) : QueryGenerator {
    private val modeCombinationStrategy by lazy { com.skedgo.tripkit.ModeCombinationStrategy() }
    override fun apply(query: Query, transportModeFilter: TransportModeFilter): Observable<List<Query>> {
        val departure = query.fromLocation
                ?: return Observable.error(NullPointerException("Departure is null"))

        val arrival = query.toLocation
                ?: return Observable.error(NullPointerException("Arrival is null"))
        return Observable
                .fromCallable { query }
                .flatMap { query ->
                    if (query.region == null) {
                        tripRegionResolver.apply(departure, arrival)
                                .doOnNext {
                                    query.region = it
                                }
                    } else {
                        Observable.just(query.region)
                    }
                            .map { it to query }
                }
                .flatMap { (region, query) ->
                    val allTransportModes = region.transportModeIds!!

//                     when {
//                        query.transportModeIds.isNotEmpty() -> region.transportModeIds!!
//                        else -> {
//                            region.transportModeIds!!
//                        }
//                    }

                    if (allTransportModes.contains(TransportMode.ID_WALK) && !allTransportModes.contains(TransportMode.ID_WHEEL_CHAIR)) {
                        allTransportModes.add(TransportMode.ID_WHEEL_CHAIR)
                    }

                    val filteredTransportModes = allTransportModes
                        .filter { transportModeFilter.useTransportMode(it) }.toMutableList()

                    if(departure.locationClass == LOCATION_CLASS_SCHOOL ||
                        arrival.locationClass == LOCATION_CLASS_SCHOOL) {
                        val modeIdentifiers = (departure.modeIdentifiers.orEmpty() + arrival.modeIdentifiers.orEmpty())
                            .toSet().toList()

                        filteredTransportModes.remove(TransportMode.ID_SCHOOL_BUS)
                        filteredTransportModes.addAll(modeIdentifiers)
                    }

                    query.transportModeIds = filteredTransportModes
                    regionService.getTransportModesAsync()
                }
                .map { modeMap ->
                    val modeSets = modeCombinationStrategy.apply(
                            modeMap,
                            query.transportModeIds
                    )

                    val queries = ArrayList<Query>(modeSets.size)
                    for (modeSet in modeSets) {
                        val clone = query.clone()
                        clone.transportModeIds.addAll(modeSet)
                        queries.add(clone)
                    }
                    queries
                }
    }

    private val tripRegionResolver: com.skedgo.tripkit.TripRegionResolver by lazy { com.skedgo.tripkit.TripRegionResolver.create(regionService) }
}