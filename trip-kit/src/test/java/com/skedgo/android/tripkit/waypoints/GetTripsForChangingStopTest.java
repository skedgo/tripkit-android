package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.ServiceStop;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.BuildConfig;
import com.skedgo.android.tripkit.TestRunner;
import com.skedgo.android.tripkit.TimetableEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class GetTripsForChangingStopTest {

  @Mock private WaypointService waypointService;
  @Mock private  WaypointSegmentAdapterUtils waypointSegmentAdapterUtils;
  private GetTripsForChangingStopImpl getTripsForChangingStop;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    getTripsForChangingStop = new GetTripsForChangingStopImpl(waypointService, waypointSegmentAdapterUtils);
  }

  @Test public void shouldGetUpdatedTrips() {

    TripSegment segment = mock(TripSegment.class);
    List<TripSegment> tripSegmentList = Collections.singletonList(segment);

    ConfigurationParams configurationParams = mock(ConfigurationParams.class);

    Location location = mock(Location.class);

    WaypointSegmentAdapter segmentAdapter = mock(WaypointSegmentAdapter.class);
    List<WaypointSegmentAdapter> segmentAdapterList = Collections.singletonList(segmentAdapter);

    when(waypointSegmentAdapterUtils.adaptStopSegmentList(segment, location, true, tripSegmentList))
        .thenReturn(segmentAdapterList);

    TripGroup updatedSegment = mock(TripGroup.class);
    List<TripGroup> tripGroups = Collections.singletonList(updatedSegment);

    when(waypointService
             .fetchChangedTripAsync(configurationParams, segmentAdapterList))
        .thenReturn(Observable.just(tripGroups));

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    getTripsForChangingStop.getTrips(tripSegmentList, segment, location, true, configurationParams)
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(tripGroups);

  }

  @Test public void shouldFailOnGetUpdatedTrips() {

    TripSegment segment = mock(TripSegment.class);
    List<TripSegment> tripSegmentList = Collections.singletonList(segment);

    ConfigurationParams configurationParams = mock(ConfigurationParams.class);

    Location location = mock(Location.class);

    WaypointSegmentAdapter segmentAdapter = mock(WaypointSegmentAdapter.class);
    List<WaypointSegmentAdapter> segmentAdapterList = Collections.singletonList(segmentAdapter);

    when(waypointSegmentAdapterUtils.adaptStopSegmentList(segment, location, true, tripSegmentList))
        .thenReturn(segmentAdapterList);

    when(waypointService
             .fetchChangedTripAsync(configurationParams, segmentAdapterList))
        .thenReturn(Observable.<List<TripGroup>>error(new Exception()));

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    getTripsForChangingStop.getTrips(tripSegmentList, segment, location, true, configurationParams)
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(Exception.class);
    subscriber.assertNoValues();

  }

}
