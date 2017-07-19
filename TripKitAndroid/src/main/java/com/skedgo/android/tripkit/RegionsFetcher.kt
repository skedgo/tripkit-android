package com.skedgo.android.tripkit

import rx.Observable

internal interface RegionsFetcher {
  fun fetchAsync(): Observable<Void>
}