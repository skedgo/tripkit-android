package com.skedgo.tripkit

import android.content.res.Resources

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup

internal class TripUpdaterImpl(
    private val resources: Resources,
    private val api: TripUpdateApi,
    private val gson: Gson) : com.skedgo.tripkit.TripUpdater {

  override fun getUpdateAsync(tripUrl: String): Observable<Trip> {
    val trip = Trip()
    trip.updateURL = tripUrl
    return getUpdateAsync(trip)
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
          .filter { it.isNotEmpty() }
          .filter { it.first().displayTrip != null}
          .map {
            it.first().displayTrip!!
          }
          .subscribeOn(Schedulers.io())
    }
  }
}