package com.skedgo.tripkit

import io.reactivex.Completable

internal interface RegionsFetcher {
    fun fetchAsync(): Completable
}