package com.skedgo.tripkit.data.database.locations.bikepods

import com.skedgo.tripkit.data.database.TripKitDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import skedgo.tripkit.location.GeoPoint


class BikePodRepositoryImpl(val tripGoDatabase2: TripKitDatabase) : BikePodRepository {
  override fun saveBikePods(key: String, bikePods: List<BikePodLocationEntity>): Completable {
    return Completable
        .fromAction {
          bikePods.forEach {
            it.cellId = key
            it.identifier = it.bikePod.identifier
          }
          tripGoDatabase2.bikePodDao().saveAll(bikePods)
        }
        .subscribeOn(Schedulers.io())
  }

  override fun getBikePods(cellIds: List<String>): Observable<List<BikePodLocationEntity>> {
    return tripGoDatabase2.bikePodDao()
        .getAllInCells(cellIds)
        .subscribeOn(Schedulers.io()).toObservable()
  }

  override fun getBikePodsWithinBounds(cellIds: List<String>, southwest: GeoPoint, northEast: GeoPoint): Observable<List<BikePodLocationEntity>> {
    return tripGoDatabase2.bikePodDao().getAllByCellsAndLatLngBounds(cellIds, southwest.latitude, southwest.longitude, northEast.latitude, northEast.longitude)
        .subscribeOn(Schedulers.io()).toObservable()
  }

  override fun getBikePod(id: String): Single<BikePodLocationEntity> {
    return Single
        .fromCallable {
          tripGoDatabase2.bikePodDao().getById(id)
        }
        .subscribeOn(Schedulers.io())
  }
}