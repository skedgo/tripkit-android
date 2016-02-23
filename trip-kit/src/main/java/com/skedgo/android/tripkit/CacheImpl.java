package com.skedgo.android.tripkit;

import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

final class CacheImpl<TData> implements Cache<TData> {
  private final AtomicReference<TData> memory = new AtomicReference<>();
  private final Observable<TData> fetcher;
  private final Observable<TData> loader;
  private Func1<TData, Boolean> isAvailable = new Func1<TData, Boolean>() {
    @Override public Boolean call(TData data) {
      return data != null;
    }
  };

  /**
   * @param fetcher Fetches data from network and always saves it to disk.
   * @param loader  Always loads data from disk. It shouldn't cache data in memory.
   */
  CacheImpl(Observable<Void> fetcher, Observable<TData> loader) {
    this.fetcher = fetcher
        .map(new Func1<Void, TData>() {
          @Override public TData call(Void unused) {
            return null;
          }
        })
        .ignoreElements()
        .replay().refCount();
    this.loader = loader
        /* Cache in memory. */
        .doOnNext(new Action1<TData>() {
          @Override public void call(TData data) {
            memory.set(data);
          }
        })
        .replay().refCount();
  }

  /**
   * Asks from memory first. If no data is available, asks loader to load data from disk.
   * If still no data on disk, continues to ask fetcher for data from network.
   * Fetcher will fetch data and save it to disk as its responsibility.
   * Then re-asks loader to load data saved by fetcher above.
   */
  @Override public Observable<TData> getAsync() {
    return Observable
        .concat(Observable.just(memory.get()), loader, fetcher, loader)
        .first(isAvailable);
  }

  /**
   * Clears memory cache.
   */
  @Override public void invalidate() {
    memory.set(null);
  }
}