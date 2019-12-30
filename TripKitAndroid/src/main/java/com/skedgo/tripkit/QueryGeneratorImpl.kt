package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.Query
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
                    val allTransportModes = when {
                        query.transportModeIds.isNotEmpty() -> query.transportModeIds
                        else -> {
                            region.transportModeIds!!
                        }
                    }
                    val filteredTransportModes = transportModeFilter.filterModes(allTransportModes)
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