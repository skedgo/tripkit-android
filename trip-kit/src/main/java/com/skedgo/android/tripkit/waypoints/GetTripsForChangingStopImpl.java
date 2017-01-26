package com.skedgo.android.tripkit.waypoints;

import android.text.TextUtils;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.waypoints.ImmutableWaypointSegmentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class GetTripsForChangingStopImpl implements GetTripsForChangingStop {

  private final WaypointService waypointService;

  @Inject public GetTripsForChangingStopImpl(WaypointService waypointService) {
    this.waypointService = waypointService;
  }

  public Single<List<TripGroup>> getTrips(final ArrayList<TripSegment> segments,
                                          final TripSegment prototypeSegment,
                                          final Location waypoint,
                                          final boolean isGetOn,
                                          final ConfigurationParams configurationParams) {

    final List<WaypointSegmentAdapter> waypointSegments = adaptSegmentList(prototypeSegment, waypoint, isGetOn, segments);

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

  // TODO: move this logic to tripkit when moving TimetableEntry
  private List<WaypointSegmentAdapter> adaptSegmentList(TripSegment prototypeSegment,
                                                        Location waypoint, boolean isGetOn,
                                                        ArrayList<TripSegment> segments) {
    List<WaypointSegmentAdapter> waypointSegments = new ArrayList<>(segments.size());

    boolean changeNextDeparture = false;
    boolean isTimeAdded = false;

    String start;
    String end;
    List<String> modes = Collections.emptyList();
    int startTime = 0;

    for (TripSegment segment : segments) {

      if (segment.getId() == prototypeSegment.getId()) {
        if (isGetOn) {
          // The waypoint now becomes the departure.
          start = waypoint.getCoordinateString();
          end = segment.getTo().getCoordinateString();
        } else {
          // Get-off case.
          start = segment.getFrom().getCoordinateString();

          // The waypoint now becomes the arrival.
          end = waypoint.getCoordinateString();

          // This case we have to change next segment's departure.
          changeNextDeparture = true;
        }

        if (!TextUtils.isEmpty(segment.getTransportModeId())) {

          modes = Collections.singletonList(segment.getTransportModeId());

        }

        if (!isTimeAdded) {
          startTime = (int) segment.getStartTimeInSecs();

          // We only add once.
          isTimeAdded = true;
        }

        waypointSegments.add(ImmutableWaypointSegmentAdapter.builder()
                                 .start(start)
                                 .end(end)
                                 .modes(modes)
                                 .startTime(startTime)
                                 .build());

      } else if ((segment.getType() != SegmentType.STATIONARY)
          && (segment.getType() != SegmentType.ARRIVAL)
          && (segment.getType() != SegmentType.DEPARTURE)) {

        start = segment.getFrom().getCoordinateString();
        end = segment.getTo().getCoordinateString();
        modes = Collections.singletonList(segment.getTransportModeId());

        if (changeNextDeparture) {
          // We've iterated at the segment following the prototype segment.
          start = waypoint.getCoordinateString();

          // We only change once.
          changeNextDeparture = true;
        }

        if (!isTimeAdded) {
          startTime = (int) segment.getStartTimeInSecs();

          // We only add once.
          isTimeAdded = true;
        }

        waypointSegments.add(ImmutableWaypointSegmentAdapter.builder()
                                 .start(start)
                                 .end(end)
                                 .modes(modes)
                                 .startTime(startTime)
                                 .build());
      }

    }
    return waypointSegments;
  }

}
