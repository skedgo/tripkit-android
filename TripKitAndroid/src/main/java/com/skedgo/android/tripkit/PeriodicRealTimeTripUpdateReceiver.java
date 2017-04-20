package com.skedgo.android.tripkit;

import skedgo.tripkit.routing.Trip;
import com.skedgo.android.common.model.TripGroup;

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

  @Override public Observable<TripGroup> startAsync() {
    return Observable.interval(initialDelay(), period(), timeUnit())
        .flatMap(new Func1<Long, Observable<Trip>>() {
          @Override public Observable<Trip> call(Long interval) {
            final Trip displayTrip = group().getDisplayTrip();
            return tripUpdater().getUpdateAsync(displayTrip)
                .onErrorResumeNext(Observable.<Trip>empty());
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<Trip, TripGroup>() {
          @Override public TripGroup call(Trip tripUpdate) {
            final Trip displayTrip = group().getDisplayTrip();
            if (displayTrip != null) {
              // FIXME: Just ditch the old display trip after receiving realtime data.
              // See more https://www.flowdock.com/app/skedgo/tripgo-v4/threads/WhEA69BC4SQNO2f7qcPHUw2kQNq.

              displayTrip.setStartTimeInSecs(tripUpdate.getStartTimeInSecs());
              displayTrip.setEndTimeInSecs(tripUpdate.getEndTimeInSecs());
              displayTrip.setSaveURL(tripUpdate.getSaveURL());
              displayTrip.setUpdateURL(tripUpdate.getUpdateURL());
              displayTrip.setProgressURL(tripUpdate.getProgressURL());
              displayTrip.setCarbonCost(tripUpdate.getCarbonCost());
              displayTrip.setMoneyCost(tripUpdate.getMoneyCost());
              displayTrip.setHassleCost(tripUpdate.getHassleCost());
              displayTrip.setSegments(tripUpdate.getSegments());

            }
            return group();
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