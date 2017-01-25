package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.TripGroup;

import java.util.List;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class WaypointsUpdater {

  private final WaypointService service;

  @Inject WaypointsUpdater(WaypointService service) {
    this.service = service;
  }

  Single<List<TripGroup>> updateWaypointsForChangingSource() {
    return Single
        .create(new Single.OnSubscribe<List<TripGroup>>() {
          @Override public void call(SingleSubscriber<? super List<TripGroup>> singleSubscriber) {


            updateWaypoints( singleSubscriber);

          }
        });
  }

  private void updateWaypoints(final SingleSubscriber<? super List<TripGroup>> singleSubscriber) {

    /*service.fetchChangedTripAsync(postData)
        .subscribe(new Action1<List<TripGroup>>() {
          @Override public void call(List<TripGroup> tripGroups) {
            singleSubscriber.onSuccess(tripGroups);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            singleSubscriber.onError(throwable);
          }
        });*/
  }
}
