package com.skedgo.android.tripkit;

import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

abstract class DataLoader<TData> implements Func0<Observable<TData>> {
  private final AtomicReference<TData> memoryCache = new AtomicReference<>();

  @Override
  public synchronized Observable<TData> call() {
    final Observable<TData> fromMemory = Observable.just(memoryCache.get());
    final Observable<TData> fromDisk = getDataAsync().subscribeOn(Schedulers.io());
    return Observable.concat(fromMemory, fromDisk)
        .first(new Func1<TData, Boolean>() {
          @Override public Boolean call(TData data) {
            return data != null;
          }
        })
        .doOnNext(new Action1<TData>() {
          @Override public void call(TData data) {
            memoryCache.set(data);
          }
        });
  }

  public void invalidate() {
    memoryCache.set(null);
  }

  protected abstract Observable<TData> getDataAsync();
}