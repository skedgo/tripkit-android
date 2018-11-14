package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.Region
import rx.Observable
import rx.functions.Func1
import java.util.*

internal class QueryGeneratorImpl(private val regionService: RegionService) : QueryGenerator {
  private val modeCombinationStrategy by lazy { ModeCombinationStrategy() }
  private val tripRegionResolver: TripRegionResolver by lazy { TripRegionResolver.create(regionService) }

  override fun call(query: Query, transportModeFilter: TransportModeFilter): Observable<List<Query>> {
    val departure = query.fromLocation
        ?: return Observable.error(NullPointerException("Departure is null"))

    val arrival = query.toLocation
        ?: return Observable.error(NullPointerException("Arrival is null"))
    return Observable
        .fromCallable { query }
        .flatMap { query ->
          if (query.region == null) {
            tripRegionResolver.call(departure, arrival)
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
          val modeSets = modeCombinationStrategy.call(
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
}