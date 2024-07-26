package com.skedgo.tripkit.data.database.locations.bikepods

import com.skedgo.tripkit.location.GeoPoint
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface BikePodRepository {
    fun saveBikePods(key: String, bikePods: List<BikePodLocationEntity>): Completable
    fun getBikePods(cellIds: List<String>): Observable<List<BikePodLocationEntity>>
    fun getBikePod(id: String): Single<BikePodLocationEntity>
    fun getBikePodsWithinBounds(
        cellIds: List<String>,
        southwest: GeoPoint,
        northEast: GeoPoint
    ): Observable<List<BikePodLocationEntity>>
}