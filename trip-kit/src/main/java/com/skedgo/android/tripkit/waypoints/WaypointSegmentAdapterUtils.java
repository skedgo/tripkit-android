package com.skedgo.android.tripkit.waypoints;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.TimetableEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class WaypointSegmentAdapterUtils {

  @Inject WaypointSegmentAdapterUtils() {}

  @VisibleForTesting
  List<WaypointSegmentAdapter> adaptServiceSegmentList(TripSegment prototypeSegment,
                                                       TimetableEntry service,
                                                       Region region,
                                                       List<TripSegment> segments) {

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

  @VisibleForTesting List<WaypointSegmentAdapter> adaptStopSegmentList(TripSegment prototypeSegment,
                                                                       Location waypoint, boolean isGetOn,
                                                                       List<TripSegment> segments) {
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
