package com.skedgo.tripkit.data.database.locations.facility

import com.skedgo.tripkit.data.database.TripKitDatabase
import com.skedgo.tripkit.location.GeoPoint
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FacilityRepositoryImpl(val tripGoDatabase2: TripKitDatabase) : FacilityRepository {
    override fun saveFacilities(
        key: String,
        facilities: List<FacilityLocationEntity>
    ): Completable {
        return Completable
            .fromAction {
                facilities.forEach {
                    it.cellId = key
                    it.identifier = it.identifier
                }
                tripGoDatabase2.facilityDao().saveAll(facilities)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getFacilities(cellIds: List<String>): Observable<List<FacilityLocationEntity>> {
        return tripGoDatabase2.facilityDao()
            .getAllInCells(cellIds)
            .subscribeOn(Schedulers.io()).toObservable()
    }

    override fun getFacilitiesWithinBounds(
        cellIds: List<String>,
        southwest: GeoPoint,
        northEast: GeoPoint
    ): Observable<List<FacilityLocationEntity>> {
        return tripGoDatabase2.facilityDao().getAllInCells(cellIds)
            .subscribeOn(Schedulers.io()).toObservable()
    }

    override fun getFacility(id: String): Single<FacilityLocationEntity> {
        return Single
            .fromCallable {
                tripGoDatabase2.facilityDao().getById(id)
            }
            .subscribeOn(Schedulers.io())
    }
}