package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.tripkit.tsp.Paratransit
import com.skedgo.android.tripkit.tsp.RegionInfo
import com.skedgo.android.tripkit.tsp.RegionInfoService
import rx.Observable
import skedgo.tripkit.routing.ModeInfo
import java.util.*
import javax.inject.Provider

internal class RegionServiceImpl(
    private val regionCache: Cache<List<Region>>,
    private val modeCache: Cache<Map<String, TransportMode>>,
    private val regionsFetcher: RegionsFetcher,
    private val regionInfoServiceProvider: Provider<RegionInfoService>,
    private val regionFinder: RegionFinder) : RegionService {
  override fun getRegionsAsync(): Observable<List<Region>> = regionCache.async

  override fun getRegionByNameAsync(regionName: String): Observable<Region> =
      getRegionsAsync()
          .flatMapIterable { it }
          .takeFirst { it.name == regionName }

  override fun getTransportModesAsync(): Observable<Map<String, TransportMode>> =
      modeCache.async

  override fun getRegionByLocationAsync(latitude: Double, longitude: Double): Observable<Region> =
      getRegionsAsync()
          .flatMap { regions -> Observable.from(regions) }
          .first { region -> regionFinder.contains(region, latitude, longitude) }
          .onErrorResumeNext { error ->
            if (error is NoSuchElementException)
              Observable.error<Region>(OutOfRegionsException(
                  "Location lies outside covered area",
                  latitude,
                  longitude
              ))
            else
              Observable.error<Region>(error)
          }

  override fun getRegionByLocationAsync(location: Location?): Observable<Region> =
      if (location == null)
        Observable.error<Region>(NullPointerException("Location is null"))
      else
        getRegionByLocationAsync(location.lat, location.lon)

  override fun getCitiesAsync(): Observable<Location> =
      getRegionsAsync()
          .flatMap { regions -> Observable.from(regions) }
          .compose(Utils.getCities())

  override fun getCitiesByNameAsync(name: String?): Observable<Location> =
      getCitiesAsync().filter(Utils.matchCityName(name))

  override fun getTransportModeByIdAsync(modeId: String): Observable<TransportMode> =
      getTransportModesAsync()
          .flatMap {
            val mode: TransportMode? = it[modeId]
            mode?.let {
              Observable.just(mode)
            } ?: Observable.empty()
          }

  override fun getTransportModesByIdsAsync(modeIds: List<String>): Observable<List<TransportMode>> =
      getTransportModesAsync().map(Utils.findModesByIds(modeIds))

  override fun getTransportModesByRegionAsync(region: Region): Observable<List<TransportMode>> {
    val modeIds = region.transportModeIds
    return if (modeIds != null)
      getTransportModesByIdsAsync(modeIds)
    else
      Observable.just(emptyList<TransportMode>())
  }

  override fun getTransportModesByLocationAsync(
      location: Location
  ): Observable<List<TransportMode>> =
      getRegionByLocationAsync(location)
          .flatMap { region ->
            val modeIds = region.transportModeIds
            if (modeIds != null)
              getTransportModesByIdsAsync(modeIds)
            else
              Observable.just(emptyList<TransportMode>())
          }

  override fun refreshAsync(): Observable<Void> =
      regionsFetcher.fetchAsync()
          .doOnCompleted {
            regionCache.invalidate()
            modeCache.invalidate()
            regionFinder.invalidate()
          }

  override fun fetchParatransitByRegionAsync(region: Region): Observable<Paratransit> =
      getRegionInfoByRegionAsync(region)
          .map { regionInfo -> regionInfo.paratransit() }

  override fun getRegionInfoByRegionAsync(region: Region): Observable<RegionInfo> =
      regionInfoServiceProvider.get()
          .fetchRegionInfoAsync(region.urLs, region.name)

  override fun getTransitModesByRegionAsync(region: Region): Observable<List<ModeInfo>> =
      getRegionInfoByRegionAsync(region)
          .map { regionInfo -> regionInfo.transitModes() }
}
