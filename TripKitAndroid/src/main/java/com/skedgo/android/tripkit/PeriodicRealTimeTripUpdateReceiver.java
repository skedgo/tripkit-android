package com.skedgo.android.tripkit;


import kotlin.Pair;
import skedgo.tripkit.android.TripKit;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import org.immutables.value.Value;

import java.util.concurrent.TimeUnit;

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
    return Observable.interval(initialDelay(), period(), timeUnit())
        .flatMap(new Func1<Long, Observable<Trip>>() {
          @Override public Observable<Trip> call(Long interval) {
            final Trip displayTrip = group().getDisplayTrip();
            return tripUpdater().getUpdateAsync(displayTrip)
                .onErrorResumeNext(Observable.<Trip>empty());
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<Trip, Pair<Trip, TripGroup>>() {
          @Override public Pair<Trip, TripGroup> call(Trip trip) {
            group().getDisplayTrip().setUpdateURL(trip.getUpdateURL());
            return new Pair<>(trip, group());
          }
        })
        .takeUntil(stop.asObservable());
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