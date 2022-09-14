package com.skedgo.tripkit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;


abstract class DataLoader<TData> implements Callable<Observable<TData>> {
  private final AtomicReference<TData> memoryCache = new AtomicReference<>();

  @Override
  public synchronized Observable<TData> call() {
    final Observable<TData> fromMemory = (memoryCache.get() == null) ? Observable.empty() : Observable.just(memoryCache.get());
    final Observable<TData> fromDisk = getDataAsync().subscribeOn(Schedulers.io());
    return Observable.concat(fromMemory, fromDisk)
            .filter(data -> data != null)
        .firstOrError().toObservable()
        .doOnNext(new Consumer<TData>() {
          @Override public void accept(TData data) {
            memoryCache.set(data);
          }
        });
  }

  public void invalidate() {
    memoryCache.set(null);
  }

  protected abstract Observable<TData> getDataAsync();
}