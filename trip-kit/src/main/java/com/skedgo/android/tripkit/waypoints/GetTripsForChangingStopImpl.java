package com.skedgo.android.tripkit.waypoints;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

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

  @VisibleForTesting List<WaypointSegmentAdapter> adaptSegmentList(TripSegment prototypeSegment,
                                                                   Location waypoint, boolean isGetOn,
                                                                   ArrayList<TripSegment> segments) {
    List<WaypointSegmentAdapter> waypointSegments = new ArrayList<>(segments.size());

    boolean changeNextDeparture = false;
    boolean isTimeAdded = false;

    for (int i = 0; i < segments.size(); i++) {

      TripSegment segment = segments.get(i);

      ImmutableWaypointSegmentAdapter.Builder builder = ImmutableWaypointSegmentAdapter.builder();

      if (segment.getId() == prototypeSegment.getId()) {
        if (isGetOn) {
          // The waypoint now becomes the departure.
          builder.start(waypoint.getCoordinateString());
          builder.end(segment.getTo().getCoordinateString());
        } else {
          // Get-off case.
          builder.start(segment.getFrom().getCoordinateString());

          // The waypoint now becomes the arrival.
          builder.end(waypoint.getCoordinateString());

          // This case we have to change next segment's departure.
          changeNextDeparture = true;
        }

        if (!TextUtils.isEmpty(segment.getTransportModeId())) {
          builder.modes(Collections.singletonList(segment.getTransportModeId()));
        }

        if (!isTimeAdded) {
          builder.startTime((int) segment.getStartTimeInSecs());

          // We only add once.
          isTimeAdded = true;
        }

        waypointSegments.add(builder.build());

      } else if ((segment.getType() != SegmentType.STATIONARY)
          && (segment.getType() != SegmentType.ARRIVAL)
          && (segment.getType() != SegmentType.DEPARTURE)) {

        if (changeNextDeparture) {
          // We've iterated at the segment following the prototype segment.
          builder.start(waypoint.getCoordinateString());

          // We only change once.
          changeNextDeparture = true;
        } else {
          builder.start(segment.getFrom().getCoordinateString());
        }

        // When is get on, check if next segment is the one that changes, meaning "end" changes here
        if (isGetOn && i < segments.size() - 1 && segments.get(i + 1).getId() == prototypeSegment.getId()) {
          builder.end(waypoint.getCoordinateString());
        } else {
          builder.end(segment.getTo().getCoordinateString());
        }

        builder.modes(Collections.singletonList(segment.getTransportModeId()));

        if (!isTimeAdded) {
          builder.startTime((int) segment.getStartTimeInSecs());

          // We only add once.
          isTimeAdded = true;
        }

        waypointSegments.add(builder.build());
      }

    }
    return waypointSegments;
  }

}
