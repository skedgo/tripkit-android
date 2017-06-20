package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.tripkit.tsp.Paratransit
import com.skedgo.android.tripkit.tsp.RegionInfo

import rx.Observable
import skedgo.tripkit.routing.ModeInfo

interface RegionService {
  fun getRegionsAsync(): Observable<List<Region>>
  fun getRegionByNameAsync(regionName: String): Observable<Region>
  fun getRegionByLocationAsync(latitude: Double, longitude: Double): Observable<Region>
  fun getRegionByLocationAsync(location: Location?): Observable<Region>
  fun getCitiesAsync(): Observable<Location>
  fun getCitiesByNameAsync(name: String?): Observable<Location>
  fun getTransportModesAsync(): Observable<Map<String, TransportMode>>
  fun getTransportModeByIdAsync(modeId: String): Observable<TransportMode>
  fun getTransportModesByIdsAsync(modeIds: List<String>): Observable<List<TransportMode>>
  fun getTransportModesByRegionAsync(region: Region): Observable<List<TransportMode>>
  fun getTransportModesByLocationAsync(location: Location): Observable<List<TransportMode>>
  fun refreshAsync(): Observable<Void>
  fun getRegionInfoByRegionAsync(region: Region): Observable<RegionInfo>
  fun fetchParatransitByRegionAsync(region: Region): Observable<Paratransit>
  fun getTransitModesByRegionAsync(region: Region): Observable<List<ModeInfo>>
}