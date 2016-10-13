package com.skedgo.android.common.util;

import android.content.res.Resources;
import android.text.TextUtils;

import com.skedgo.android.common.R;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.TripSegment;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import rx.functions.Action1;

/**
 * Puts a Departure segment before head of,
 * and puts an Arrival segment after tail of a segment list.
 * <p/>
 * Also, fills identifiers for segments.
 */
public class TripSegmentListResolver {
  private Location origin;
  private Location destination;
  private List<TripSegment> tripSegmentList;
  private AtomicLong segmentIdGenerator;
  private Resources resources;

  public TripSegmentListResolver(Resources resources) {
    segmentIdGenerator = new AtomicLong();
    this.resources = resources;
  }

  /**
   * @param origin The location that we depart
   */
  public TripSegmentListResolver setOrigin(Location origin) {
    this.origin = origin;
    return this;
  }

  /**
   * @param destination The location that we finally arrive
   */
  public TripSegmentListResolver setDestination(Location destination) {
    this.destination = destination;
    return this;
  }

  public TripSegmentListResolver setTripSegmentList(List<TripSegment> tripSegmentList) {
    this.tripSegmentList = tripSegmentList;
    return this;
  }

  public void resolve() {
    if (!CollectionUtils.isEmpty(tripSegmentList)) {
      putDepartureSegment();
      putArrivalSegment();

      fillSegmentIdentifiers();
    }
  }

  /**
   * Creates the Arrival segment from the last segment
   *
   * @param lastSegment The last segment (or tail) of the segment list
   * @return The Arrival segment
   */
  public TripSegment createArrivalSegment(TripSegment lastSegment) {
    String destinationName = TripSegmentUtils.getLocationName(destination);
    String arrivalAction;

    if (TextUtils.isEmpty(destinationName)) {
      arrivalAction = String.format(resources.getString(R.string.arrivallocation), resources.getString(R.string.destination));
    } else {
      arrivalAction = String.format(resources.getString(R.string.arrivallocationtime), destinationName, "<TIME>");
    }

    final TripSegment arrivalSegment = new TripSegment();
    arrivalSegment.setType(SegmentType.ARRIVAL);
    arrivalSegment.setFrom(destination);
    arrivalSegment.setTo(destination);
    arrivalSegment.setAction(arrivalAction);
    arrivalSegment.setVisibility(TripSegment.VISIBILITY_ON_MAP);
    arrivalSegment.setStartTimeInSecs(lastSegment.getEndTimeInSecs());
    arrivalSegment.setEndTimeInSecs(lastSegment.getEndTimeInSecs());
    lastSegment.whenTimeChanged().subscribe(new Action1<TripSegment>() {
      @Override
      public void call(TripSegment segment) {
        arrivalSegment.setStartTimeInSecs(segment.getEndTimeInSecs());
        arrivalSegment.setEndTimeInSecs(segment.getEndTimeInSecs());
      }
    });
    return arrivalSegment;
  }

  /**
   * Creates the Departure segment from the first segment
   *
   * @param firstSegment The first segment (or head) of the segment list
   * @return The Departure segment
   */
  public TripSegment createDepartureSegment(final TripSegment firstSegment) {
    String originName = TripSegmentUtils.getLocationName(origin);

    String departureAction;
    if (TextUtils.isEmpty(originName)) {
      departureAction = String.format(resources.getString(R.string.leavelocation), resources.getString(R.string.origin));
    } else {
      departureAction = String.format(resources.getString(R.string.leavelocationtime), originName, "<TIME>");
    }

    final TripSegment departureSegment = new TripSegment();
    departureSegment.setType(SegmentType.DEPARTURE);
    departureSegment.setFrom(origin);
    departureSegment.setTo(origin);
    departureSegment.setAction(departureAction);
    departureSegment.setVisibility(TripSegment.VISIBILITY_IN_DETAILS);
    departureSegment.setStartTimeInSecs(firstSegment.getStartTimeInSecs());
    departureSegment.setEndTimeInSecs(firstSegment.getStartTimeInSecs());
    firstSegment.whenTimeChanged().subscribe(new Action1<TripSegment>() {
      @Override
      public void call(TripSegment segment) {
        departureSegment.setStartTimeInSecs(segment.getStartTimeInSecs());
        departureSegment.setEndTimeInSecs(segment.getStartTimeInSecs());
      }
    });
    return departureSegment;
  }

  private void fillSegmentIdentifiers() {
    segmentIdGenerator.set(0L);
    long newSegmentId;
    for (TripSegment segment : tripSegmentList) {
      newSegmentId = segmentIdGenerator.incrementAndGet();
      segment.setId(newSegmentId);
    }
  }

  /**
   * Puts a Departure segment before head
   */
  private void putArrivalSegment() {
    TripSegment lastSegment = tripSegmentList.get(tripSegmentList.size() - 1);
    if (lastSegment != null) {
      TripSegment arrivalSegment = createArrivalSegment(lastSegment);
      tripSegmentList.add(arrivalSegment);
    }
  }

  /**
   * Puts an Arrival segment after tail
   */
  private void putDepartureSegment() {
    TripSegment firstSegment = tripSegmentList.get(0);
    if (firstSegment != null) {
      TripSegment departureSegment = createDepartureSegment(firstSegment);
      tripSegmentList.add(0, departureSegment);
    }
  }
}