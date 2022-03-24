package com.skedgo.tripkit

import io.reactivex.*
import io.reactivex.schedulers.Schedulers.io
import java.util.concurrent.atomic.AtomicReference

/**
 * @param fetcher Fetches data from network and always saves it to disk.
 * @param loader  Always loads data from disk. It shouldn't cache data in memory.
 */
internal class CacheImpl<TData>(
    private val fetcher: Completable,
    loader: Observable<TData>
) : com.skedgo.tripkit.Cache<TData> {
  private val memory = AtomicReference<TData>()
  fun getMemory(): Maybe<TData> {
    return if (memory.get() != null) {
      Maybe.just(memory.get())
    } else {
      Maybe.empty()
    }
  }

  fun loadFromDb(): Single<TData> {
    return loader
            .switchIfEmpty(Maybe.defer {
              fetcher.andThen(loader) } )
            .toSingle()
  }

  private val loader: Maybe<TData>

  init {
    this.loader = loader
            .onErrorResumeNext(Observable.empty())
            .doOnNext { data -> memory.set(data) }
            .firstElement()
  }

  /**
   * Asks from memory first. If no data is available, asks loader to load data from disk.
   * If still no data on disk, continues to ask fetcher for data from network.
   * Fetcher will fetch data and save it to disk as its responsibility.
   * Then re-asks loader to load data saved by fetcher above.
   */
  override fun getAsync(): Single<TData> {
    return getMemory()
            .switchIfEmpty(loadFromDb())
            .subscribeOn(io())
  }

  /**
   * Clears memory cache.
   */
  override fun invalidate() = memory.set(null)

}