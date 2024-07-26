package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.Query
import io.reactivex.Observable
import io.reactivex.functions.BiFunction


interface QueryGenerator :
    BiFunction<Query, TransportModeFilter, Observable<@JvmSuppressWildcards List<Query>>> {
}