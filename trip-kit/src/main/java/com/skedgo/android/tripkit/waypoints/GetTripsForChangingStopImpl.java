package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

import java.util.List;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class GetTripsForChangingStopImpl implements GetTripsForChangingStop {

  private final WaypointService waypointService;
  private final WaypointSegmentAdapterUtils waypointSegmentAdapterUtils;

  @Inject
  public GetTripsForChangingStopImpl(WaypointService waypointService, WaypointSegmentAdapterUtils waypointSegmentAdapterUtils) {
    this.waypointService = waypointService;
    this.waypointSegmentAdapterUtils = waypointSegmentAdapterUtils;
  }

  public Single<List<TripGroup>> getTrips(final List<TripSegment> segments,
                                          final TripSegment prototypeSegment,
                                          final Location waypoint,
                                          final boolean isGetOn,
                                          final ConfigurationParams configurationParams) {

    final List<WaypointSegmentAdapter> waypointSegments = waypointSegmentAdapterUtils.adaptStopSegmentList(prototypeSegment, waypoint, isGetOn, segments);

    return Single
        .create(new Single.OnSubscribe<List<TripGroup>>() {
          @Override
          public void call(final SingleSubscriber<? super List<TripGroup>> singleSubscriber) {
            waypointService.fetchChangedTripAsync(configurationParams, waypointSegments)
                .subscribe(new Action1<List<TripGroup>>() {
                  @Override public void call(List<TripGroup> tripGroups) {
                    singleSubscriber.onSuccess(tripGroups);
                  }
                }, new Action1<Throwable>() {
                  @Override public void call(Throwable throwable) {
                    singleSubscriber.onError(throwable);
                  }
                });

          }
        });

  }

}
