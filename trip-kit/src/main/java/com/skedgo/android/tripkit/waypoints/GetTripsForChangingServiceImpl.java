package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.TimetableEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class GetTripsForChangingServiceImpl implements GetTripsForChangingService {

  private final WaypointService waypointService;

  @Inject public GetTripsForChangingServiceImpl(WaypointService waypointService) {
    this.waypointService = waypointService;
  }

  public Single<List<TripGroup>> getTrips(final Region region, final ArrayList<TripSegment> segments,
                                          final TripSegment prototypeSegment,
                                          final TimetableEntry service,
                                          final ConfigurationParams configurationParams) {

    final List<WaypointSegmentAdapter> waypointSegments = adaptSegmentList(prototypeSegment, service, region, segments);

    return Single
        .create(new Single.OnSubscribe<List<TripGroup>>() {
          @Override
          public void call(final SingleSubscriber<? super List<TripGroup>> singleSubscriber) {

            waypointService.fetchChangedTripAsync(/*((ConfigCreator) configCreator).callWrapper()*/
                                                  configurationParams, waypointSegments)
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

  // TODO: move this logic to tripkit when moving TimetableEntry
  private List<WaypointSegmentAdapter> adaptSegmentList(TripSegment prototypeSegment,
                                                        TimetableEntry service,
                                                        Region region,
                                                        ArrayList<TripSegment> segments) {

    List<WaypointSegmentAdapter> waypointSegments = new ArrayList<>(segments.size());

    for (TripSegment segment : segments) {
      if (segment.getId() == prototypeSegment.getId()) {

        ArrayList<String> modes = new ArrayList<>(2);
        modes.add("pt_pub");
        modes.add("pt_sch");

        waypointSegments.add(ImmutableWaypointSegmentAdapter.builder()
                                 .start(service.getStopCode())
                                 .end(service.getEndStopCode())
                                 .modes(modes)
                                 .startTime((int) service.getStartTimeInSecs())
                                 .endTime((int) service.getEndTimeInSecs())
                                 .operator(service.getOperator())
                                 .region(region.getName())
                                 .build());

      } else if ((segment.getType() != SegmentType.STATIONARY)
          && (segment.getType() != SegmentType.ARRIVAL)
          && (segment.getType() != SegmentType.DEPARTURE)) {
        waypointSegments.add(ImmutableWaypointSegmentAdapter.builder()
                                 .start(segment.getFrom().getCoordinateString())
                                 .end(segment.getTo().getCoordinateString())
                                 .modes(Collections.singletonList(segment.getTransportModeId()))
                                 .build());
      }
    }
    return waypointSegments;
  }

}
