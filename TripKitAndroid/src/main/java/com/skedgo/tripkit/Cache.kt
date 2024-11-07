package com.skedgo.tripkit

import io.reactivex.Single

internal interface Cache<TData> {
    fun getAsync(): Single<TData>

    fun invalidate()
}