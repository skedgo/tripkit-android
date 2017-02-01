package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class GetTripsForChangingStopImpl implements GetTripByChangingStop {

  private final WaypointService waypointService;
  private final WaypointSegmentAdapterUtils waypointSegmentAdapterUtils;

  @Inject
  public GetTripsForChangingStopImpl(WaypointService waypointService, WaypointSegmentAdapterUtils waypointSegmentAdapterUtils) {
    this.waypointService = waypointService;
    this.waypointSegmentAdapterUtils = waypointSegmentAdapterUtils;
  }

  @Override
  public Single<List<TripGroup>> call(@NotNull final List<? extends TripSegment> segments,
                                      @NotNull final TripSegment prototypeSegment,
                                      @NotNull final Location waypoint,
                                      @NotNull final boolean isGetOn,
                                      @NotNull final ConfigurationParams configurationParams) {

    final List<WaypointSegmentAdapter> waypointSegments =
        waypointSegmentAdapterUtils.adaptStopSegmentList(prototypeSegment, waypoint, isGetOn, segments);

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
