package com.skedgo.tripkit

import android.content.Context
import android.text.TextUtils
import android.util.Log

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.tripkit.tsp.RegionInfoRepository

import java.util.HashMap
import java.util.concurrent.TimeUnit

import androidx.collection.ArrayMap
import com.skedgo.tripkit.tsp.hasWheelChairInformation
import io.reactivex.Observable
import com.skedgo.tripkit.a2brouting.FailoverA2bRoutingApi
import com.skedgo.tripkit.a2brouting.RouteService
import com.skedgo.tripkit.a2brouting.ToWeightingProfileString
import com.skedgo.tripkit.data.tsp.RegionInfo
import skedgo.tripkit.routing.TripGroup

internal class RouteServiceImpl(
        private val context: Context,
        private val queryGenerator: QueryGenerator,
        private val co2Preferences: com.skedgo.tripkit.Co2Preferences?,
        private val tripPreferences: com.skedgo.tripkit.TripPreferences?,
        private val extraQueryMapProvider: com.skedgo.tripkit.routing.ExtraQueryMapProvider?,
        private val routingApi: FailoverA2bRoutingApi,
        private val regionInfoRepository: RegionInfoRepository) : RouteService {

  private fun toCoordinatesText(location: Location): String {
    val coordinatesText = StringBuilder("(" + location.lat + "," + location.lon + ")")
    if (!TextUtils.isEmpty(location.address)) {
      coordinatesText.append("\"").append(location.address).append("\"")
    }
    return coordinatesText.toString()
  }

  override fun routeAsync(
      query: Query,
      transportModeFilter: TransportModeFilter,
      transitModeFilter: TransitModeFilter): Observable<List<TripGroup>> {
    return flatSubQueries(query, transportModeFilter)
        .concatMap { subQuery ->
          regionInfoRepository.getRegionInfoByRegion(subQuery.region!!)
              .map { regionInfo -> Pair(subQuery, regionInfo) }
        }
        .flatMap { (subQuery, regionInfo) ->
          val region = subQuery.region
          val baseUrls = region!!.urLs
          val modes = subQuery.transportModeIds
          val excludeStops = subQuery.excludedStopCodes
          val allTransitModes = regionInfo.transitModes().orEmpty().map { it.id!! }

          // TODO This was causing the entire chain to hang sometimes, but I'm not sure why.
          // Temporarily grabbing the excluded modes directly
//          val excludedTransitModes = allTransitModes
//              .subtract(transitModeFilter.filterTransitModes(regionInfo).map {
//                it.id!! })
//              .toList()

          val prefs = context.getSharedPreferences("IsModeIncludedInTripsPrefs", Context.MODE_PRIVATE)
          val excluded = prefs.all
                  .filter {
                    it.value as Boolean
                  }
                  .map {
                    it.key as String
                  }
          val excludedTransitModes = allTransitModes
              .subtract(excluded)
              .toList()

          val options = toOptions(subQuery, regionInfo)
          routingApi.fetchRoutesAsync(baseUrls, modes, excludedTransitModes, excludeStops, options)
        }
  }

  /* TODO: Consider making this public for Xerox team. */
  fun getParamsByPreferences(regionInfo: RegionInfo): Map<String, Any> {
    val map = ArrayMap<String, Any>()
    if (tripPreferences != null) {
      if (tripPreferences.isConcessionPricingPreferred) {
        map["conc"] = true
      }
      if (tripPreferences.isWheelchairPreferred && regionInfo.hasWheelChairInformation()) {
        map["wheelchair"] = true
      }
    }

    if (co2Preferences != null) {
      val co2Profile = co2Preferences.co2Profile
      for ((key, value) in co2Profile) {
        map["co2[$key]"] = value
      }
    }

    return map
  }

  fun toOptions(query: Query, regionInfo: RegionInfo): Map<String, Any> {
    val departureCoordinates = toCoordinatesText(query.fromLocation!!)
    val arrivalCoordinates = toCoordinatesText(query.toLocation!!)
    val arriveBefore = TimeUnit.MILLISECONDS.toSeconds(query.arriveBy)
    val departAfter = TimeUnit.MILLISECONDS.toSeconds(query.departAfter)
    val unit = query.unit
    val transferTime = query.transferTime
    val walkingSpeed = query.walkingSpeed
    val cyclingSpeed = query.cyclingSpeed

    val options = HashMap<String, Any>()
    options["from"] = departureCoordinates
    options["to"] = arrivalCoordinates
    options["arriveBefore"] = java.lang.Long.toString(arriveBefore)
    options["departAfter"] = java.lang.Long.toString(departAfter)
    options["unit"] = unit
    options["v"] = "12"
    options["tt"] = Integer.toString(transferTime)
    options["ws"] = Integer.toString(walkingSpeed)
    options["cs"] = Integer.toString(cyclingSpeed)
    options["includeStops"] = "1"
    options["wp"] = ToWeightingProfileString.toWeightingProfileString(query)
    options.putAll(getParamsByPreferences(regionInfo))
    if (extraQueryMapProvider != null) {
      val extraQueryMap = extraQueryMapProvider.call()
      options.putAll(extraQueryMap)
    }
    return options
  }

  private fun flatSubQueries(query: Query, transportModeFilter: TransportModeFilter): Observable<Query> {
    return queryGenerator.apply(query, transportModeFilter)
        .flatMap { Observable.fromIterable(it) }
  }
}