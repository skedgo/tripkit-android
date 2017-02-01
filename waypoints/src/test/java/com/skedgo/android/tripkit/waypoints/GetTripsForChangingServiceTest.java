package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Region;
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

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class GetTripsForChangingServiceTest {

  @Mock private WaypointService waypointService;
  @Mock private WaypointSegmentAdapterUtils waypointSegmentAdapterUtils;
  private GetTripsForChangingServiceImpl getTripsForChangingService;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    getTripsForChangingService = new GetTripsForChangingServiceImpl(waypointService, waypointSegmentAdapterUtils);
  }

  @Test public void shouldGetUpdatedTrips() {

    Region region = Mockito.mock(Region.class);

    TripSegment segment = Mockito.mock(TripSegment.class);
    List<TripSegment> tripSegmentList = Collections.singletonList(segment);

    TimetableEntry timetableEntry = Mockito.mock(TimetableEntry.class);

    ConfigurationParams configurationParams = Mockito.mock(ConfigurationParams.class);

    WaypointSegmentAdapter segmentAdapter = Mockito.mock(WaypointSegmentAdapter.class);
    List<WaypointSegmentAdapter> segmentAdapterList = Collections.singletonList(segmentAdapter);

    Mockito.when(waypointSegmentAdapterUtils.adaptServiceSegmentList(segment, timetableEntry, region, tripSegmentList))
        .thenReturn(segmentAdapterList);

    TripGroup updatedSegment = Mockito.mock(TripGroup.class);
    List<TripGroup> tripGroups = Collections.singletonList(updatedSegment);

    Mockito.when(waypointService
             .fetchChangedTripAsync(configurationParams, segmentAdapterList))
        .thenReturn(Observable.just(tripGroups));

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    getTripsForChangingService.getTrips(region, tripSegmentList, segment, timetableEntry, configurationParams)
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(tripGroups);

  }

  @Test public void shouldFailOnGetUpdatedTrips() {

    Region region = Mockito.mock(Region.class);

    TripSegment segment = Mockito.mock(TripSegment.class);
    List<TripSegment> tripSegmentList = Collections.singletonList(segment);

    TimetableEntry timetableEntry = Mockito.mock(TimetableEntry.class);

    ConfigurationParams configurationParams = Mockito.mock(ConfigurationParams.class);

    WaypointSegmentAdapter segmentAdapter = Mockito.mock(WaypointSegmentAdapter.class);
    List<WaypointSegmentAdapter> segmentAdapterList = Collections.singletonList(segmentAdapter);

    Mockito.when(waypointSegmentAdapterUtils.adaptServiceSegmentList(segment, timetableEntry, region, tripSegmentList))
        .thenReturn(segmentAdapterList);

    Mockito.when(waypointService
             .fetchChangedTripAsync(configurationParams, segmentAdapterList))
        .thenReturn(Observable.<List<TripGroup>>error(new Exception()));

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    getTripsForChangingService.getTrips(region, tripSegmentList, segment, timetableEntry, configurationParams)
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(Exception.class);
    subscriber.assertNoValues();

  }

}




