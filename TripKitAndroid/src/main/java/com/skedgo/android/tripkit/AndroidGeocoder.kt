package com.skedgo.android.tripkit

import android.content.Context
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.geocoding.Geocodable

class AndroidGeocoder(private val context: Context) : Geocodable {
  override fun getAddress(latitude: Double, longitude: Double): Observable<String> {
    return Observable
        .create(OnSubscribeReverseGeocode(context, latitude, longitude, 1))
        .filter { addresses -> addresses != null && !addresses.isEmpty() }
        .map { addresses ->
          // The old way used solution in
          // http://developer.android.com/training/location/display-address.html.
          // but we get empty string when getMaxAddressLineIndex = 0
          // while getAddressLine() has got their string address.
          // So that I suggest that we should calculate addressLine based on
          // the actual addressLine of address.
          val address = addresses[0]
          var addressLine = ""
          if (address.getAddressLine(0) != null) {
            addressLine = address.getAddressLine(0)
          } else {
            address.getAddressLine(0)
          }

          var index = 1
          while (address.getAddressLine(index) != null) {
            addressLine += " ${address.getAddressLine(index++)}"
          }
          addressLine
        }
        .subscribeOn(Schedulers.io())
  }
}