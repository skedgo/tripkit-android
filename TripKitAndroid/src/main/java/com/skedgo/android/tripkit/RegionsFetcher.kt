package com.skedgo.android.tripkit

import rx.Completable

internal interface RegionsFetcher {
  fun fetchAsync(): Completable
}