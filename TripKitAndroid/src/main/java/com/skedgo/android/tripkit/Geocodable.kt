package com.skedgo.android.tripkit

import rx.Observable

interface Geocodable {
  fun getAddress(latitude: Double, longitude: Double): Observable<String>
}