package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.Location
import com.skedgo.tripkit.common.model.Region
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.data.tsp.Paratransit
import com.skedgo.tripkit.data.tsp.RegionInfo
import com.skedgo.tripkit.tsp.RegionInfoRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.tsp.hasWheelChairInformation
import io.reactivex.functions.BiFunction

internal class RegionServiceImpl(
        private val regionCache: Cache<List<Region>>,
        private val modeCache: Cache<Map<String, TransportMode>>,
        private val regionsFetcher: RegionsFetcher,
        private val regionInfoRepository: RegionInfoRepository,
        private val regionFinder: RegionFinder) : RegionService {
    override fun getRegionsAsync(): Observable<List<Region>> = regionCache.async.toObservable()

    override fun getRegionByNameAsync(regionName: String): Observable<Region> =
            getRegionsAsync()
                    .flatMapIterable { it }
                    .filter { it.name == regionName }
                    .first(Region()).toObservable()

    override fun getTransportModesAsync(): Observable<Map<String, TransportMode>> =
            modeCache.async.toObservable()

    override fun getRegionByLocationAsync(latitude: Double, longitude: Double): Observable<Region> =
            getRegionsAsync()
                    .flatMap { regions ->
                        Observable.fromIterable(regions)
                    }
                    .filter { region ->
                        regionFinder.contains(region, latitude, longitude)
                    }
                    .firstOrError()
                    .onErrorResumeNext { error ->
                        if (error is NoSuchElementException)
                            Single.error<Region>(com.skedgo.tripkit.OutOfRegionsException(
                                    "Location lies outside covered area",
                                    latitude,
                                    longitude
                            ))
                        else
                            Single.error<Region>(error)
                    }.toObservable()

    override fun getRegionByLocationAsync(location: Location?): Observable<Region> =
            if (location == null)
                Observable.error<Region>(NullPointerException("Location is null"))
            else
                getRegionByLocationAsync(location.lat, location.lon)

    override fun getCitiesAsync(): Observable<Location> =
            getRegionsAsync()
                    .flatMap { regions -> Observable.fromIterable(regions) }
                    .compose(com.skedgo.tripkit.Utils.getCities())

    override fun getCitiesByNameAsync(name: String?): Observable<Location> =
            getCitiesAsync().filter(com.skedgo.tripkit.Utils.matchCityName(name))

    override fun getTransportModeByIdAsync(modeId: String): Observable<TransportMode> =
            getTransportModesAsync()
                    .flatMap {
                        val mode: TransportMode? = it[modeId]
                        mode?.let {
                            Observable.just(mode)
                        } ?: Observable.empty()
                    }

    override fun getTransportModesByIdsAsync(modeIds: List<String>): Observable<List<TransportMode>> =
            getTransportModesAsync().map(com.skedgo.tripkit.Utils.findModesByIds(modeIds))

    override fun getTransportModesByRegionAsync(region: Region): Observable<List<TransportMode>> {
        val modeIds = region.transportModeIds

        return if (modeIds != null)
            getTransportModesByIdsAsync(modeIds)
        else
            Observable.just(emptyList<TransportMode>())
    }


    override fun getTransportModesByLocationsAsync(location1: Location, location2: Location): Observable<List<TransportMode>> {
        return Observable.zip(getRegionByLocationAsync(location1), getRegionByLocationAsync(location2),
                BiFunction { first: Region, second: Region -> first to second })
                .flatMap { regionPair ->
                    Observable.zip(getRegionInfoByRegionAsync(regionPair.first), getRegionInfoByRegionAsync(regionPair.second),
                            BiFunction { regionInfoOne: RegionInfo, regionInfoTwo: RegionInfo ->
                                var transportModesOne = regionPair.first.transportModeIds
                                var transportModesTwo = regionPair.second.transportModeIds

                                if (regionInfoOne.hasWheelChairInformation() && transportModesOne?.contains(TransportMode.ID_WHEEL_CHAIR) != true) {
                                    transportModesOne?.add(TransportMode.ID_WHEEL_CHAIR)
                                }

                                if (regionInfoTwo.hasWheelChairInformation() && transportModesTwo?.contains(TransportMode.ID_WHEEL_CHAIR) != true) {
                                    transportModesTwo?.add(TransportMode.ID_WHEEL_CHAIR)
                                }
                                transportModesOne to transportModesTwo
                            })
                }
                .flatMap {
                    val list1 = it.first ?: arrayListOf<String>()
                    val list2 = it.second ?: arrayListOf<String>()

                    getTransportModesByIdsAsync(list1.union(list2).toList())
                }
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

    override fun refreshAsync(): Completable =
            regionsFetcher.fetchAsync()
                    .doOnComplete {
                        regionCache.invalidate()
                        modeCache.invalidate()
                        regionFinder.invalidate()
                    }

    override fun fetchParatransitByRegionAsync(region: Region): Observable<Paratransit> =
            getRegionInfoByRegionAsync(region)
                    .map { regionInfo -> regionInfo.paratransit() }

    override fun getRegionInfoByRegionAsync(region: Region): Observable<RegionInfo> =
            regionInfoRepository.getRegionInfoByRegion(region)

    override fun getTransitModesByRegionAsync(region: Region): Observable<List<ModeInfo>> =
            getRegionInfoByRegionAsync(region)
                    .map { regionInfo -> regionInfo.transitModes() }
}
