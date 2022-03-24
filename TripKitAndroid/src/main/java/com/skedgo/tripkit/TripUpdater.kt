package com.skedgo.tripkit

import com.skedgo.tripkit.routing.Trip
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface TripUpdater {
    fun getUpdateAsync(trip: Trip): Observable<Trip>
    fun getUpdateAsync(tripUrl: String): Observable<Trip>
    fun getUpdateAsyncFlow(tripUrl: String): Flow<Trip>
    fun getUpdateAsyncFlow(trip: Trip): Flow<Trip>
}