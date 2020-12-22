package com.skedgo.android.tripkit

import android.text.TextUtils

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.Region
import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider
import com.skedgo.android.tripkit.tsp.RegionInfo
import com.skedgo.android.tripkit.tsp.RegionInfoRepository

import java.util.Collections
import java.util.HashMap
import java.util.concurrent.TimeUnit

import androidx.collection.ArrayMap
import com.gojuno.koptional.Optional
import com.gojuno.koptional.Some
import com.skedgo.android.tripkit.tsp.hasWheelChairInformation
import rx.Observable
import rx.functions.Func1
import rx.functions.Func2
import skedgo.tripkit.a2brouting.FailoverA2bRoutingApi
import skedgo.tripkit.a2brouting.RouteService
import skedgo.tripkit.a2brouting.ToWeightingProfileString
import skedgo.tripkit.routing.TripGroup

internal class RouteServiceImpl(
    private val queryGenerator: QueryGenerator,
    private val co2Preferences: Co2Preferences?,
    private val tripPreferences: TripPreferences?,
    private val extraQueryMapProvider: ExtraQueryMapProvider?,
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
              .map { subQuery to it }
              .onErrorReturn { subQuery to null }
        }
        .flatMap { (subQuery, regionInfo) ->
          val region = subQuery.region
          val baseUrls = region!!.urLs
          val modes = subQuery.transportModeIds
          val excludeStops = subQuery.excludedStopCodes
          val allTransitModes = regionInfo.transitModes().orEmpty().map { it.id!! }
          val excludedTransitModes = allTransitModes
              .subtract(transitModeFilter.filterTransitModes(regionInfo).map { it.id!! })
              .toList()
          val options = toOptions(subQuery, regionInfo)
          routingApi.fetchRoutesAsync(baseUrls, modes, excludedTransitModes, excludeStops, options)
        }
  }

  /* TODO: Consider making this public for Xerox team. */
  fun getParamsByPreferences(regionInfo: RegionInfo?): Map<String, Any> {
    val map = ArrayMap<String, Any>()
    if (tripPreferences != null) {
      if (tripPreferences.isConcessionPricingPreferred) {
        map["conc"] = true
      }
      if (tripPreferences.isWheelchairPreferred && regionInfo != null && regionInfo.hasWheelChairInformation()) {
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

  fun toOptions(query: Query, regionInfo: RegionInfo?): Map<String, Any> {
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
    return queryGenerator.call(query, transportModeFilter)
        .flatMap { Observable.from(it) }
  }
}