package com.skedgo.tripkit

import android.content.Context
import android.text.TextUtils

import com.skedgo.tripkit.common.model.Location
import com.skedgo.tripkit.common.model.Query
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
import com.skedgo.tripkit.routing.TripGroup

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
      transportModeFilter: TransportModeFilter): Observable<List<TripGroup>> {
    return flatSubQueries(query, transportModeFilter)
          .flatMap { subQuery ->
            val region = subQuery.region
            val baseUrls = region!!.urLs
            val modes = subQuery.transportModeIds
            val excludeStops = subQuery.excludedStopCodes
            val avoidModes = region.transportModeIds.orEmpty().map { it }.filter { transportModeFilter.avoidTransportMode(it) }

            val options = toOptions(subQuery)
            routingApi.fetchRoutesAsync(baseUrls, modes, avoidModes, excludeStops, options)
        }
  }

  /* TODO: Consider making this public for Xerox team. */
  fun getParamsByPreferences(): Map<String, Any> {
    val map = ArrayMap<String, Any>()
    if (tripPreferences != null) {
      if (tripPreferences.isConcessionPricingPreferred) {
        map["conc"] = true
      }
      if (tripPreferences.isWheelchairPreferred) {
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

  fun toOptions(query: Query): Map<String, Any> {
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
    options.putAll(getParamsByPreferences())
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