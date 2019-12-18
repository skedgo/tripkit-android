package com.skedgo.tripkit;

import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.immutables.value.Value;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import kotlin.Pair;
import org.reactivestreams.Publisher;
import com.skedgo.TripKit;
import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class PeriodicRealTimeTripUpdateReceiver implements RealTimeTripUpdateReceiver {
  private final PublishSubject<Object> stop = PublishSubject.create();

  public static Builder builder() {
    return new PeriodicRealTimeTripUpdateReceiverBuilder()
        .tripUpdater(TripKit.getInstance().getTripUpdater());
  }

  @Override public Flowable<Pair<Trip, TripGroup>> startAsync() {
    return Flowable.interval(initialDelay(), period(), timeUnit(), Schedulers.trampoline())
        .map(new Function<Long, String>() {
          @Override public String apply(Long aLong) {
            return group().getDisplayTrip().getUpdateURL();
          }
        })
        .onBackpressureDrop()
        .compose(new FlowableTransformer<String, Trip>() {
         public Publisher<Trip> apply(Flowable<String> updateUrl) {
            final AtomicReference<String> url = new AtomicReference<String>();
            return updateUrl
                .flatMap((Function<String, Flowable<Trip>>) s -> {
                  final String lastUrl = url.get();
                  return tripUpdater().getUpdateAsync(lastUrl != null ? lastUrl : s)
                      .onErrorResumeNext(Observable.empty()).toFlowable(BackpressureStrategy.BUFFER);
                })
                .doOnNext(new Consumer<Trip>() {
                  @Override public void accept(Trip trip) {
                    url.set(trip.getUpdateURL());
                  }
                });
          }
        })
        .map(new Function<Trip, Pair<Trip, TripGroup>>() {
          @Override public Pair<Trip, TripGroup> apply(Trip trip) {
            return new Pair<>(trip, group());
          }
        })
        .takeUntil(stop.toFlowable(BackpressureStrategy.BUFFER))
        .subscribeOn(Schedulers.io());
  }

  @Override public void stop() {
    stop.onNext(new Object());
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
