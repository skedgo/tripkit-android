package com.skedgo.android.tripkit

import android.content.res.Resources
import android.text.TextUtils

import com.google.gson.Gson
import skedgo.tripkit.routing.RoutingResponse
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.routing.TripGroup

import org.apache.commons.collections4.CollectionUtils

import rx.Observable
import rx.functions.Func1
import rx.schedulers.Schedulers

internal class TripUpdaterImpl(
    private val resources: Resources,
    private val api: TripUpdateApi,
    private val gson: Gson) : TripUpdater {

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
      api.fetchUpdateAsync(updateURL!!)
          .map { response ->
            response.processRawData(resources, gson)
            response.tripGroupList
          }
          .filter { it != null && it.isNotEmpty() }
          .map { it.first().displayTrip }
          .filter { it != null }
          .map { it!! }
          .subscribeOn(Schedulers.io())
    }
  }
}