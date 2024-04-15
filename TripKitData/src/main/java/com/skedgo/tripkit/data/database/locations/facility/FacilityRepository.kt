package com.skedgo.tripkit.data.database.locations.facility

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import com.skedgo.tripkit.location.GeoPoint

interface FacilityRepository {
  fun saveFacilities(key: String, facilities: List<FacilityLocationEntity>): Completable
  fun getFacilities(cellIds: List<String>): Observable<List<FacilityLocationEntity>>
  fun getFacility(id: String): Single<FacilityLocationEntity>
  fun getFacilitiesWithinBounds(cellIds: List<String>, southwest: GeoPoint, northEast: GeoPoint): Observable<List<FacilityLocationEntity>>
}