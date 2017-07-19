package com.skedgo.android.tripkit

import rx.Completable
import rx.Observable
import rx.functions.Func1
import java.util.concurrent.atomic.AtomicReference

/**
 * @param fetcher Fetches data from network and always saves it to disk.
 * @param loader  Always loads data from disk. It shouldn't cache data in memory.
 */
internal class CacheImpl<TData>(
    fetcher: Completable,
    loader: Observable<TData>
) : Cache<TData> {
  private val memory = AtomicReference<TData>()
  private val fetcher: Observable<TData>
  private val loader: Observable<TData>
  private val isAvailable = Func1<TData, Boolean> { data -> data != null }

  init {
    this.fetcher = fetcher
        .toObservable<TData>()
        .replay().refCount()
    this.loader = loader
        /* Cache in memory. */
        .doOnNext { data -> memory.set(data) }
        .replay().refCount()
  }

  /**
   * Asks from memory first. If no data is available, asks loader to load data from disk.
   * If still no data on disk, continues to ask fetcher for data from network.
   * Fetcher will fetch data and save it to disk as its responsibility.
   * Then re-asks loader to load data saved by fetcher above.
   */
  override fun getAsync(): Observable<TData> =
      Observable
          .concat(Observable.just(memory.get()), loader, fetcher, loader)
          .first(isAvailable)

  /**
   * Clears memory cache.
   */
  override fun invalidate() = memory.set(null)
}
