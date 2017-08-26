package com.skedgo.android.tripkit;

import android.util.Log;

import kotlin.Pair;
import rx.Observer;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;
import skedgo.tripkit.android.TripKit;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import org.immutables.value.Value;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class PeriodicRealTimeTripUpdateReceiver implements RealTimeTripUpdateReceiver {
  private final PublishSubject<Void> stop = PublishSubject.create();

  public static Builder builder() {
    return new PeriodicRealTimeTripUpdateReceiverBuilder()
        .tripUpdater(TripKit.getInstance().getTripUpdater());
  }

  @Override public Observable<Pair<Trip, TripGroup>> startAsync() {
    return Observable.interval(initialDelay(), period(), timeUnit(), Schedulers.trampoline())
        .map(new Func1<Long, String>() {
          @Override public String call(Long aLong) {
            return group().getDisplayTrip().getUpdateURL();
          }
        })
        .onBackpressureDrop()
        .compose(new Observable.Transformer<String, Trip>() {
          @Override public Observable<Trip> call(Observable<String> updateUrl) {
            final AtomicReference<String> url = new AtomicReference<String>();
            return updateUrl
                .flatMap(new Func1<String, Observable<Trip>>() {
                  @Override public Observable<Trip> call(String s) {
                    final String lastUrl = url.get();
                    return tripUpdater().getUpdateAsync(lastUrl != null ? lastUrl : s)
                        .onErrorResumeNext(Observable.<Trip>empty());
                  }
                })
                .doOnNext(new Action1<Trip>() {
                  @Override public void call(Trip trip) {
                    url.set(trip.getUpdateURL());
                  }
                });
          }
        })
        .map(new Func1<Trip, Pair<Trip, TripGroup>>() {
          @Override public Pair<Trip, TripGroup> call(Trip trip) {
            return new Pair<>(trip, group());
          }
        })
        .takeUntil(stop.asObservable())
        .subscribeOn(Schedulers.io());
  }

  @Override public void stop() {
    stop.onNext(null);
  }

  abstract TripUpdater tripUpdater();
  abstract TripGroup group();
  abstract int initialDelay();
  abstract int period();
  abstract TimeUnit timeUnit();

  public interface Builder {
    Builder group(TripGroup group);
    Builder initialDelay(int initialDelay);
    Builder period(int period);
    Builder timeUnit(TimeUnit timeUnit);
    RealTimeTripUpdateReceiver build();
  }
}