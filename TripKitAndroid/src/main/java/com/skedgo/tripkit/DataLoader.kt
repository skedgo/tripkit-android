package com.skedgo.tripkit

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicReference

abstract class DataLoader<TData> : Callable<Observable<TData>> {
    val memoryCache = AtomicReference<TData>()

    @Synchronized
    override fun call(): Observable<TData> {
        val fromMemory = memoryCache.get().let { Observable.just(it) } ?: Observable.empty()
        val fromDisk = getDataAsync().subscribeOn(Schedulers.io())
        return Observable.concat(fromMemory, fromDisk)
            .filter { it != null }
            .firstOrError()
            .toObservable()
            .doOnNext { data -> memoryCache.set(data) }
    }

    fun invalidate() {
        memoryCache.set(null)
    }

    protected abstract fun getDataAsync(): Observable<TData>
}