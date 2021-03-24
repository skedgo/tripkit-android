package com.skedgo.tripkit.data.database.locations.freefloating

import com.skedgo.tripkit.data.database.TripKitDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import com.skedgo.tripkit.location.GeoPoint


class FreeFloatingRepositoryImpl(val tripGoDatabase2: TripKitDatabase) : FreeFloatingRepository {
  override fun saveFreeFloatingLocations(key: String, freeFloatingLocations: List<FreeFloatingLocationEntity>): Completable {
    return Completable
        .fromAction {
            freeFloatingLocations.forEach {
                it.cellId = key
                it.identifier = it.vehicle.identifier
          }
          tripGoDatabase2.freeFloatingLocationDao().saveAll(freeFloatingLocations)
        }
        .subscribeOn(Schedulers.io())
  }

  override fun getFreeFloatingLocations(cellIds: List<String>): Observable<List<FreeFloatingLocationEntity>> {
    return tripGoDatabase2.freeFloatingLocationDao()
        .getAllInCells(cellIds)
        .subscribeOn(Schedulers.io()).toObservable()
  }

  override fun getFreeFloatingLocationsWithinBounds(cellIds: List<String>, southwest: GeoPoint, northEast: GeoPoint): Observable<List<FreeFloatingLocationEntity>> {
      return tripGoDatabase2.freeFloatingLocationDao().getAllInCells(cellIds)
        .subscribeOn(Schedulers.io()).toObservable()
  }

  override fun getFreeFloatingLocation(id: String): Single<FreeFloatingLocationEntity> {
    return Single
        .fromCallable {
          tripGoDatabase2.freeFloatingLocationDao().getById(id)
        }
        .subscribeOn(Schedulers.io())
  }
}