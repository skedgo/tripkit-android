package com.skedgo.tripkit.data.locations
import com.skedgo.tripkit.common.model.Region
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodRepository
import com.skedgo.tripkit.data.database.locations.carparks.CarParkMapper
import com.skedgo.tripkit.data.database.locations.carparks.CarParkPersistor
import com.skedgo.tripkit.data.database.locations.carpods.CarPodMapper
import com.skedgo.tripkit.data.database.locations.carpods.CarPodRepository
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingMapper
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingPersistor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.apache.commons.collections4.CollectionUtils
import com.skedgo.tripkit.agenda.ConfigRepository
import java.util.*

open class StopsFetcher(private val api: LocationsApi,
                        private val cellsLoader: ICellsLoader,
                        private val cellsPersistor: ICellsPersistor,
                        private val stopsPersistor: IStopsPersistor,
                        private val configCreator: ConfigRepository,
                        private val bikePodRepository: BikePodRepository,
                        private val carParkPersistor: CarParkPersistor,
                        private val onStreetParkingPersistor: OnStreetParkingPersistor,
                        private val carParkMapper: CarParkMapper,
                        private val carPodMapper: CarPodMapper,
                        private val onStreetParkingMapper: OnStreetParkingMapper,
                        private val carPodRepository: CarPodRepository) {

  open fun fetchAsync(cellIds: List<String>,
                      region: Region,
                      level: Int): Observable<List<LocationsResponse.Group>> {
    return fetchCellsAsync(cellIds, region, level)
        .filter { CollectionUtils.isNotEmpty(it) }
        .flatMap { this.saveCellsAsync(it) }
  }

  private fun createRequestBodiesAsync(cellIds: List<String>,
                                       region: Region,
                                       level: Int): Observable<LocationsRequestBody> {
    return cellsLoader.loadSavedCellsAsync(cellIds)
        .defaultIfEmpty(emptyList())
        .flatMap { existingCells ->
          splitIntoBodiesForNewFetchOrUpdate(
              cellIds,
              existingCells,
              region,
              level
          )
        }
        .subscribeOn(Schedulers.newThread())
  }

  /**
   * @param existingCells Cells found in database
   */
  internal fun splitIntoBodiesForNewFetchOrUpdate(cellIds: List<String>,
                                                  existingCells: List<LocationsResponse.Group>,
                                                  region: Region,
                                                  level: Int): Observable<LocationsRequestBody>  {
    return Observable.create { subscriber ->
      if (CollectionUtils.isEmpty(existingCells)) {
        // Given cells are completely new. No cells saved yet.
        subscriber.onNext(LocationsRequestBody.createForNewlyFetching(
            region,
            ArrayList(cellIds),
            level,
            configCreator.call()
        ))
      } else {
        // Exclude saved cells out of given cells.
        val newCellIds = ArrayList(cellIds)
        for (existingCell in existingCells) {
          newCellIds.remove(existingCell.key)
        }
        if (CollectionUtils.isNotEmpty(newCellIds)) {
          // No point in emitting empty cell list.
          subscriber.onNext(LocationsRequestBody.createForNewlyFetching(
              region,
              newCellIds,
              level,
              configCreator.call()
          ))
        }

        // For cells that were already requested before.
        val cellIdsAndHashCodes = HashMap<String, Long>()
        for (existingCell in existingCells) {
          cellIdsAndHashCodes[existingCell.key] = existingCell.hashCode
        }
        subscriber.onNext(LocationsRequestBody.createForUpdating(
            region,
            cellIdsAndHashCodes,
            level,
            configCreator.call()
        ))
      }

      subscriber.onComplete()
    }
  }

  private fun fetchCellsAsync(cellIds: List<String>,
                              region: Region,
                              level: Int): Observable<List<LocationsResponse.Group>>  {
    return createRequestBodiesAsync(cellIds, region, level)
        .flatMap { body ->
          val baseUrl = region.urLs!![0]
          val url = baseUrl.toHttpUrlOrNull()!!
              .newBuilder()
              .addPathSegment("locations.json")
              .build()
          fetchCellsAsync(url.toString(), body)
        }
  }

  private fun fetchCellsAsync(url: String, requestBody: LocationsRequestBody): Observable<List<LocationsResponse.Group>>  {
    return api.fetchLocationsAsync(url, requestBody)
        .filter { response -> response != null && CollectionUtils.isNotEmpty(response.groups) }
        .map { it.groups }
  }

  private fun saveCellsAsync(cells: List<LocationsResponse.Group>): Observable<List<LocationsResponse.Group>>  {
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
        .plus(
            cells.filter { it.bikePods != null && it.bikePods.isNotEmpty() }
                .map { bikePodRepository.saveBikePods(it.key, it.bikePods) }
        ).plus(
            cells.filter { it.carPods != null && it.carPods.isNotEmpty() }
                .map { carPodRepository.saveCarPods(carPodMapper.toEntity(it.key, it.carPods)) }
        ).plus(
            cells.filter { it.carParks != null && it.carParks.isNotEmpty() }
                .map { carParkPersistor.saveCarParks(carParkMapper.toEntity(it.key, it.carParks)) }
        )
        .plus(
            cells.filter { it.onStreetParkings != null && it.onStreetParkings.isNotEmpty() }
                .map {
                  onStreetParkingPersistor.saveOnStreetParkings(
                      onStreetParkingMapper.toEntity(it.key, it.onStreetParkings))
                }
        )
        .let {
          Completable.merge(it)
        }
        .toObservable()
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