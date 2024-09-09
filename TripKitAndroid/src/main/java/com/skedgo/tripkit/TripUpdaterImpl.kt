package com.skedgo.tripkit

import android.content.res.Resources
import com.google.gson.Gson
import com.haroldadmin.cnradapter.NetworkResponse
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class TripUpdaterImpl(
    private val resources: Resources,
    private val api: TripUpdateApi,
    private val gson: Gson
) : com.skedgo.tripkit.TripUpdater {

    override fun getUpdateAsync(tripUrl: String): Observable<Trip> {
        val trip = Trip()
        trip.updateURL = tripUrl
        return getUpdateAsync(trip)
    }

    override fun getUpdateAsyncFlow(tripUrl: String): Flow<Trip> {
        val trip = Trip()
        trip.updateURL = tripUrl
        return getUpdateAsyncFlow(trip)
    }

    override fun getUpdateAsyncFlow(trip: Trip): Flow<Trip> {
        val updateURL = trip.updateURL
        if (updateURL.isNullOrEmpty()) {
            return flow { throw RuntimeException("Empty updateURL") }
        }
        return flow {
            val response = api.fetchUpdateAsyncKt(updateURL)
            when (response) {
                is NetworkResponse.Success -> {
                    response.body.processRawData(resources, gson)
                    emit(response.body.tripGroupList ?: listOf<TripGroup>())
                }

                else -> throw response.toException()
            }
        }.filter { it.isNotEmpty() }
            .filter { it.firstOrNull()?.displayTrip != null }
            .map { it.first().displayTrip!! }

    }

    override fun getUpdateAsync(trip: Trip): Observable<Trip> {
        val updateURL = trip.updateURL
        return if (updateURL.isNullOrEmpty()) {
            Observable.empty()
        } else {
            api.fetchUpdateAsync(updateURL)
                .map { response ->
                    response.processRawData(resources, gson)
                    response.tripGroupList ?: listOf<TripGroup>()
                }
                .filter {
                    it.isNotEmpty()
                }
                .filter { it.first().displayTrip != null }
                .map {
                    it.first().displayTrip!!
                }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun tripSubscription(url: String): Observable<Any> =
        api.tripSubscription(url).subscribeOn(Schedulers.io())
}