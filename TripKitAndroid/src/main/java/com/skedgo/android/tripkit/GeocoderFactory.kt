package com.skedgo.android.tripkit

import android.content.Context
import android.text.TextUtils
import rx.Observable
import rx.schedulers.Schedulers

class GeocoderFactory(private val context: Context) : Geocodable {
  override fun getAddress(latitude: Double, longitude: Double): Observable<String> {
    return Observable
        .create(OnSubscribeReverseGeocode(context, latitude, longitude, 1))
        .filter { addresses -> addresses != null && !addresses.isEmpty() }
        .map { addresses ->
          // See http://developer.android.com/training/location/display-address.html.
          val address = addresses[0]
          val addressLines = arrayOfNulls<String>(address.maxAddressLineIndex)
          for (i in addressLines.indices) {
            addressLines[i] = address.getAddressLine(i)
          }
          TextUtils.join(" ", addressLines)
        }
        .subscribeOn(Schedulers.io())
  }
}