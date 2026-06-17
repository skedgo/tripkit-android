package com.skedgo.tripkit.data.locations
import com.google.gson.Gson
import com.skedgo.tripkit.agenda.ConfigRepository
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodRepository
import com.skedgo.tripkit.data.database.locations.carparks.CarParkMapper
import com.skedgo.tripkit.data.database.locations.carparks.CarParkPersistor
import com.skedgo.tripkit.data.database.locations.carpods.CarPodMapper
import com.skedgo.tripkit.data.database.locations.carpods.CarPodRepository
import com.skedgo.tripkit.data.database.locations.facility.FacilityRepository
import com.skedgo.tripkit.data.database.locations.freefloating.FreeFloatingRepository
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingMapper
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingPersistor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.apache.commons.collections4.CollectionUtils
import java.util.regex.Pattern
import java.util.concurrent.TimeUnit

open class StopsFetcher(
    private val api: LocationsApi,
    private val cellsLoader: ICellsLoader,
    private val cellsPersistor: ICellsPersistor,
    private val stopsPersistor: IStopsPersistor,
    private val configCreator: ConfigRepository,
    private val bikePodRepository: BikePodRepository,
    private val freeFloatingRepository: FreeFloatingRepository,
    private val carParkPersistor: CarParkPersistor,
    private val onStreetParkingPersistor: OnStreetParkingPersistor,
    private val carParkMapper: CarParkMapper,
    private val carPodMapper: CarPodMapper,
    private val onStreetParkingMapper: OnStreetParkingMapper,
    private val carPodRepository: CarPodRepository,
    private val facilityRepository: FacilityRepository,
    private val fetchCoordinator: LocationsFetchCoordinator = LocationsFetchCoordinator(),
) {
    private val urlFallbackStaggerMs = 400L
    private val gridCellIdPattern = Pattern.compile("^-?\\d+#-?\\d+$")

    open fun fetchAsync(
        cellIds: List<String>,
        region: Region,
        level: Int
    ): Observable<List<LocationsResponse.Group>> {
        if (cellIds.isEmpty()) return Observable.empty()
        val regionName = region.name ?: return Observable.empty()

        // Skip cells already fetched within the TTL window. This is the main defence against
        // repeated /satapp/locations.json calls for the same area (#25753).
        val staleCellIds = fetchCoordinator.filterStaleCellIds(cellIds, regionName, level)
        if (staleCellIds.isEmpty()) return Observable.empty()

        // Share concurrent requests for the same key (viewport pipeline + buffered prefetch
        // routinely fire within milliseconds of each other from MapViewModel).
        val inFlightKey = buildInFlightKey(regionName, level, staleCellIds)
        return fetchCoordinator.shareInFlight(inFlightKey) {
            fetchCellsAsync(staleCellIds, region, level)
                // Record every requested cell as fetched, regardless of whether the response
                // actually contained data for it. Empty responses MUST be cached too — that
                // was the root cause of the API explosion for sparse regions.
                .doOnNext { fetchCoordinator.recordFetched(staleCellIds, regionName, level) }
                .filter { CollectionUtils.isNotEmpty(it) }
                .flatMap { this.saveCellsAsync(it) }
        }
    }

    private fun buildInFlightKey(regionName: String, level: Int, cellIds: List<String>): String {
        // Sorted for stability so call order does not affect dedup.
        return "$regionName|$level|" + cellIds.sorted().joinToString(",")
    }

    private fun createRequestBodiesAsync(
        cellIds: List<String>,
        region: Region,
        level: Int
    ): Observable<LocationsRequestBody> {
        return cellsLoader.loadSavedCellsAsync(cellIds)
            .defaultIfEmpty(emptyList())
            .flatMap { existingCells ->
                splitIntoBodiesForNewFetchOrUpdate(
                    cellIds,
                    existingCells, // Fix: Use existing cells to enable hash code validation for performance
                    region,
                    level
                )
            }
            .subscribeOn(Schedulers.newThread())
    }

    /**
     * @param existingCells Cells found in database
     */
    internal fun splitIntoBodiesForNewFetchOrUpdate(
        cellIds: List<String>,
        existingCells: List<LocationsResponse.Group>,
        region: Region,
        level: Int
    ): Observable<LocationsRequestBody> {
        return Observable.create { subscriber ->
            if (CollectionUtils.isEmpty(existingCells)) {
                // Given cells are completely new. No cells saved yet.
                subscriber.onNext(
                    LocationsRequestBody.createForNewlyFetching(
                        region,
                        ArrayList(cellIds),
                        level,
                        configCreator.call()
                    )
                )
            } else {
                // Exclude saved cells out of given cells.
                val newCellIds = ArrayList(cellIds)
                for (existingCell in existingCells) {
                    newCellIds.remove(existingCell.key)
                }
                if (CollectionUtils.isNotEmpty(newCellIds)) {
                    // No point in emitting empty cell list.
                    subscriber.onNext(
                        LocationsRequestBody.createForNewlyFetching(
                            region,
                            newCellIds,
                            level,
                            configCreator.call()
                        )
                    )
                }

                // For cells that were already requested before.
                val cellIdsAndHashCodes = HashMap<String, Long>()
                for (existingCell in existingCells) {
                    cellIdsAndHashCodes[existingCell.key] = existingCell.hashCode
                }
                if (cellIds.all(::isGridCellId)) {
                    subscriber.onNext(
                        LocationsRequestBody.createForUpdating(
                            region,
                            cellIdsAndHashCodes,
                            level,
                            configCreator.call()
                        )
                    )
                } else if (CollectionUtils.isEmpty(newCellIds)) {
                    // Non-grid ids (e.g. region keys like AU_NT_Darwin) can be rejected by
                    // some backends when sent as cellIDHashCodes-only update payloads.
                    subscriber.onNext(
                        LocationsRequestBody.createForNewlyFetching(
                            region,
                            ArrayList(cellIds),
                            level,
                            configCreator.call()
                        )
                    )
                }
            }

            subscriber.onComplete()
        }
    }

    private fun isGridCellId(cellId: String): Boolean {
        return gridCellIdPattern.matcher(cellId).matches()
    }

    private fun fetchCellsAsync(
        cellIds: List<String>,
        region: Region,
        level: Int
    ): Observable<List<LocationsResponse.Group>> {
        return createRequestBodiesAsync(cellIds, region, level)
            .flatMap { body ->
                val urls = region.getURLs().orEmpty()
                    .mapNotNull { baseUrl ->
                        baseUrl.toHttpUrlOrNull()
                            ?.newBuilder()
                            ?.addPathSegment("locations.json")
                            ?.build()
                            ?.toString()
                    }

                if (urls.isEmpty()) {
                    Observable.just(emptyList())
                } else {
                    fetchCellsFromAny(urls, body)
                        .flatMap { groups ->
                            if (shouldForceFullFetchAfterEmptyUpdate(body, groups, cellIds)) {
                                val fullFetchBody = LocationsRequestBody.createForNewlyFetching(
                                    region,
                                    ArrayList(cellIds),
                                    level,
                                    configCreator.call()
                                )
                                fetchCellsFromAny(urls, fullFetchBody)
                            } else {
                                Observable.just(groups)
                            }
                        }
                }
            }
    }

    private fun shouldForceFullFetchAfterEmptyUpdate(
        requestBody: LocationsRequestBody,
        groups: List<LocationsResponse.Group>,
        requestedCellIds: List<String>
    ): Boolean {
        val existingCells = requestBody.existingCells ?: return false
        if (requestBody.cellIds != null) return false
        if (groups.isNotEmpty()) return false
        // Only fallback when this was an update-only request for the entire requested set.
        return existingCells.size == requestedCellIds.size && requestedCellIds.isNotEmpty()
    }

    private fun fetchCellsAsync(
        url: String,
        requestBody: LocationsRequestBody
    ): Observable<List<LocationsResponse.Group>> {
        return api.fetchLocationsAsync(url, requestBody)
            .filter { response ->
                response != null && CollectionUtils.isNotEmpty(response.groups)
            }
            .map { it.groups }
    }

    private fun fetchCellsFromAny(
        urls: List<String>,
        requestBody: LocationsRequestBody
    ): Observable<List<LocationsResponse.Group>> {
        val requests = urls.mapIndexed { index, url ->
            fetchCellsAsync(
                url = url,
                requestBody = requestBody
            )
                .delaySubscription(index * urlFallbackStaggerMs, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(Observable.empty())
        }

        return Observable.merge(requests)
            .take(1)
            .switchIfEmpty(Observable.just(emptyList()))
    }

    private fun saveCellsAsync(cells: List<LocationsResponse.Group>): Observable<List<LocationsResponse.Group>> {
        // Saving cell ids, hash codes and saving stops will be performed in parallel.
        return Observable.merge(
            saveCellIdsAndHashCodesAsync(cells).subscribeOn(Schedulers.newThread()),
            saveStopsAsync(cells).subscribeOn(Schedulers.newThread())
        )
    }

    private fun saveCellIdsAndHashCodesAsync(cells: List<LocationsResponse.Group>): Observable<List<LocationsResponse.Group>> {
        return Observable.create { subscriber ->
            cellsPersistor.saveCellsSync(cells)
            subscriber.onComplete()
        }
    }

    private fun saveStopsAsync(cells: List<LocationsResponse.Group>): Observable<List<LocationsResponse.Group>> {
        return Completable
            .fromAction { stopsPersistor.saveStopsSync(cells) }
            .let { listOf(it) }
            .asSequence()
            .plus(
                cells.filter { it.bikePods != null && it.bikePods.isNotEmpty() }
                    .map { bikePodRepository.saveBikePods(it.key, it.bikePods) }
            ).plus(
                cells.filter { it.carParks != null && it.carParks.isNotEmpty() }
                    .map {
                        carParkPersistor.saveCarParks(
                            carParkMapper.toEntity(
                                it.key,
                                it.carParks
                            )
                        )
                    }
            ).plus(
                cells.filter { it.freeFloating != null && it.freeFloating.isNotEmpty() }
                    .map {
                        freeFloatingRepository.saveFreeFloatingLocations(
                            it.key,
                            it.freeFloating
                        )
                    }
            ).plus(
                cells.filter { it.onStreetParkings != null && it.onStreetParkings.isNotEmpty() }
                    .map {
                        onStreetParkingPersistor.saveOnStreetParkings(
                            onStreetParkingMapper.toEntity(it.key, it.onStreetParkings)
                        )
                    }
            ).plus(
                cells.filter { it.carPods != null && it.carPods.isNotEmpty() }
                    .map { carPodRepository.saveCarPods(carPodMapper.toEntity(it.key, it.carPods)) }
            ).plus(
                cells.filter { it.facilities != null && it.facilities.isNotEmpty() }
                    .map {
                        facilityRepository.saveFacilities(it.key, it.facilities.map { it.toEntity() })
                    }
            )
            .toList()
            .let {
                Completable.merge(it)
            }
            .toObservable()
    }

    fun clearCarPods(): Completable {
        return carPodRepository.clearCarPods()
    }

    /**
     * @see [The Dependency Inversion Principle](http://www.codeproject.com/Articles/93369/How-I-explained-OOD-to-my-wife)
     */
    interface ICellsLoader {
        fun loadSavedCellsAsync(cellIds: List<String>): Observable<List<LocationsResponse.Group>>
    }

    /**
     * @see [The Dependency Inversion Principle](http://www.codeproject.com/Articles/93369/How-I-explained-OOD-to-my-wife)
     */
    interface IStopsPersistor {
        fun saveStopsSync(cells: List<@JvmSuppressWildcards LocationsResponse.Group>)
    }

    /**
     * @see [The Dependency Inversion Principle](http://www.codeproject.com/Articles/93369/How-I-explained-OOD-to-my-wife)
     */
    interface ICellsPersistor {
        fun saveCellsSync(cells: List<@JvmSuppressWildcards LocationsResponse.Group>)
    }

}