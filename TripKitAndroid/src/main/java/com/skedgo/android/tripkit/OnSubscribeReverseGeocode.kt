package com.skedgo.android.tripkit

import android.content.Context
import android.location.Address
import android.location.Geocoder

import rx.Observable
import rx.Subscriber

internal class OnSubscribeReverseGeocode(
    private val context: Context,
    private val latitude: Double,
    private val longitude: Double,
    private val maxResults: Int
) : Observable.OnSubscribe<List<Address>> {
  override fun call(subscriber: Subscriber<in List<Address>>) {
    if (!Geocoder.isPresent()) {
      subscriber.onError(UnsupportedOperationException("Geocoder not present"))
      return
    }
    val geocoder = Geocoder(context)
    try {
      subscriber.onNext(geocoder.getFromLocation(latitude, longitude, maxResults))
      subscriber.onCompleted()
    } catch (e: Exception) {
      subscriber.onError(e)
    }
  }
}