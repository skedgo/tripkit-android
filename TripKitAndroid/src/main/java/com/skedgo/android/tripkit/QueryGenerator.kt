package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Query
import rx.Observable
import rx.functions.Func1
import rx.functions.Func2

interface QueryGenerator : Func2<Query, TransportModeFilter, Observable<@JvmSuppressWildcards List<Query>>> {
}