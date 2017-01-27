package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.TimetableEntry;

import java.util.List;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class GetTripsForChangingServiceImpl implements GetTripsForChangingService {

  private final WaypointService waypointService;
  private final WaypointSegmentAdapterUtils waypointSegmentAdapterUtils;

  @Inject
  public GetTripsForChangingServiceImpl(WaypointService waypointService, WaypointSegmentAdapterUtils waypointSegmentAdapterUtils) {
    this.waypointService = waypointService;
    this.waypointSegmentAdapterUtils = waypointSegmentAdapterUtils;
  }

  public Single<List<TripGroup>> getTrips(final Region region, final List<TripSegment> segments,
                                          final TripSegment prototypeSegment,
                                          final TimetableEntry service,
                                          final ConfigurationParams configurationParams) {

    final List<WaypointSegmentAdapter> waypointSegments = waypointSegmentAdapterUtils.adaptServiceSegmentList(prototypeSegment, service, region, segments);

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
