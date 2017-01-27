package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.ServiceStop;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class GetTripsForChangingServiceTest {

  @Mock private WaypointService waypointService;
  private GetTripsForChangingServiceImpl getTripsForChangingService;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    getTripsForChangingService = new GetTripsForChangingServiceImpl(waypointService);
  }

  @Test public void shouldAdaptSegmentListService() {

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

    TripSegment taxiSegment = new TripSegment();
    taxiSegment.setId(2);
    taxiSegment.setType(SegmentType.UNSCHEDULED);
    taxiSegment.setFrom(new Location(3.0, 3.0));
    taxiSegment.setTo(new Location(4.0, 4.0));
    taxiSegment.setTransportModeId("cool_taxi");
    taxiSegment.setStartTimeInSecs(74);
    taxiSegment.setEndTimeInSecs(80);

    segments.add(busSegment);
    segments.add(taxiSegment);

    TimetableEntry timetableEntry = mock(TimetableEntry.class);
    when(timetableEntry.getStopCode()).thenReturn("stop code");
    when(timetableEntry.getEndStopCode()).thenReturn("end stop code");
    when(timetableEntry.getStartTimeInSecs()).thenReturn(10L);
    when(timetableEntry.getEndTimeInSecs()).thenReturn(20L);
    when(timetableEntry.getOperator()).thenReturn("operator");

    Region region = mock(Region.class);
    when(region.getName()).thenReturn("region");

    List<WaypointSegmentAdapter> adaptedList = getTripsForChangingService.adaptSegmentList(
        busSegment, timetableEntry, region, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    assertThat(busSegmentAdapted.start()).isEqualTo(timetableEntry.getStopCode());
    assertThat(busSegmentAdapted.end()).isEqualTo(timetableEntry.getEndStopCode());
    assertThat(busSegmentAdapted.modes()).isNotNull();
    assertThat(busSegmentAdapted.modes()).hasSize(2);
    assertThat(busSegmentAdapted.modes().get(0)).isEqualTo("pt_pub");
    assertThat(busSegmentAdapted.modes().get(1)).isEqualTo("pt_sch");
    assertThat(busSegmentAdapted.startTime()).isEqualTo((int) timetableEntry.getStartTimeInSecs());
    assertThat(busSegmentAdapted.endTime()).isEqualTo((int) timetableEntry.getEndTimeInSecs());
    assertThat(busSegmentAdapted.region()).isEqualTo(region.getName());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    assertThat(taxiSegmentAdapted.start()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    assertThat(taxiSegmentAdapted.end()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    assertThat(taxiSegmentAdapted.modes()).isNotNull();
    assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

  }


}




