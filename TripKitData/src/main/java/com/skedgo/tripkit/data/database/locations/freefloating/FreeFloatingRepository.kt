package com.skedgo.tripkit.data.database.locations.freefloating

import com.skedgo.tripkit.location.GeoPoint
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FreeFloatingRepository {
  fun saveFreeFloatingLocations(key: String, freeFloatingLocations: List<FreeFloatingLocationEntity>): Completable
  fun getFreeFloatingLocations(cellIds: List<String>): Observable<List<FreeFloatingLocationEntity>>
  fun getFreeFloatingLocation(id: String): Single<FreeFloatingLocationEntity>
  fun getFreeFloatingLocationsWithinBounds(cellIds: List<String>, southwest: GeoPoint, northEast: GeoPoint): Observable<List<FreeFloatingLocationEntity>>
}