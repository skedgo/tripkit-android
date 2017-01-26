package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.ServiceStop;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.BuildConfig;
import com.skedgo.android.tripkit.TestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class GetTripsForChangingStopTest {

  @Mock private WaypointService waypointService;
  private GetTripsForChangingStopImpl getTripsForChangingStop;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    getTripsForChangingStop = new GetTripsForChangingStopImpl(waypointService);
  }

  @Test public void shouldAdaptSegmentListStopGetOn1stSegment() {

    ArrayList<TripSegment> segments = new ArrayList<>();
    TripSegment busSegment = new TripSegment();
    busSegment.setId(1L);
    busSegment.setType(SegmentType.SCHEDULED);
    busSegment.setServiceTripId("AwesomeSydney");
    busSegment.setStartStopCode("A");
    busSegment.setEndStopCode("B");
    busSegment.setFrom(new Location(1.0, 1.0));
    busSegment.setTo(new Location(3.0, 3.0));
    busSegment.setTransportModeId("cool_bus");
    busSegment.setStartTimeInSecs(47L);
    busSegment.setEndTimeInSecs(74L);
    segments.add(busSegment);

    TripSegment taxiSegment = new TripSegment();
    taxiSegment.setId(2);
    taxiSegment.setType(SegmentType.UNSCHEDULED);
    taxiSegment.setFrom(new Location(3.0, 3.0));
    taxiSegment.setTo(new Location(4.0, 4.0));
    taxiSegment.setTransportModeId("cool_taxi");
    taxiSegment.setStartTimeInSecs(74);
    taxiSegment.setEndTimeInSecs(80);
    segments.add(taxiSegment);

    ServiceStop waypoint = new ServiceStop(new Location(2.0, 2.0));
    waypoint.setName("SydneyOperaHouse");

    List<WaypointSegmentAdapter> adaptedList = getTripsForChangingStop.adaptSegmentList(
        busSegment, waypoint, true, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    assertThat(busSegmentAdapted.start()).isEqualTo(waypoint.getCoordinateString());
    assertThat(busSegmentAdapted.end()).isEqualTo(busSegment.getTo().getCoordinateString());
    assertThat(busSegmentAdapted.modes()).isNotNull();
    assertThat(busSegmentAdapted.modes()).hasSize(1);
    assertThat(busSegmentAdapted.modes().get(0)).isEqualTo(busSegment.getTransportModeId());
    assertThat(busSegmentAdapted.startTime()).isEqualTo((int) busSegment.getStartTimeInSecs());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(0);
    assertThat(taxiSegmentAdapted.start()).isEqualTo(waypoint.getCoordinateString());
    assertThat(taxiSegmentAdapted.end()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    assertThat(taxiSegmentAdapted.modes()).isNotNull();
    assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

  }

}
