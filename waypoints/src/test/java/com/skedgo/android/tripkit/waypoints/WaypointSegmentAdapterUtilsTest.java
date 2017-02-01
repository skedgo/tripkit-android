
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
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class WaypointSegmentAdapterUtilsTest {

  private WaypointSegmentAdapterUtils waypointSegmentAdapterUtils;

  @Before public void before() {
    waypointSegmentAdapterUtils = new WaypointSegmentAdapterUtils();
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

    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptServiceSegmentList$production_sources_for_module_waypoints(
        busSegment, timetableEntry, region, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    assertThat(busSegmentAdapted.getStart()).isEqualTo(timetableEntry.getStopCode());
    assertThat(busSegmentAdapted.getEnd()).isEqualTo(timetableEntry.getEndStopCode());
    assertThat(busSegmentAdapted.getModes()).isNotNull();
    assertThat(busSegmentAdapted.getModes()).hasSize(2);
    assertThat(busSegmentAdapted.getModes().get(0)).isEqualTo("pt_pub");
    assertThat(busSegmentAdapted.getModes().get(1)).isEqualTo("pt_sch");
    assertThat(busSegmentAdapted.getStartTime()).isEqualTo((int) timetableEntry.getStartTimeInSecs());
    assertThat(busSegmentAdapted.getEndTime()).isEqualTo((int) timetableEntry.getEndTimeInSecs());
    assertThat(busSegmentAdapted.getRegion()).isEqualTo(region.getName());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    assertThat(taxiSegmentAdapted.getStart()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    assertThat(taxiSegmentAdapted.getEnd()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    assertThat(taxiSegmentAdapted.getModes()).isNotNull();
    assertThat(taxiSegmentAdapted.getModes()).hasSize(1);
    assertThat(taxiSegmentAdapted.getModes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

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

    ServiceStop waypoint = new ServiceStop(new Location(2.0, 2.0));
    waypoint.setName("SydneyOperaHouse");

    // Bus 1,1 to 3,3 + Taxi 3,3 to 4,4.
    // Get on 1st segment (Bus), in 2,2.
    // Result shoud be: Bus 2,2 to 3,3 + Taxi 3,3 to 4,4.
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList$production_sources_for_module_waypoints(
        busSegment, waypoint, true, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    assertThat(waypoint.getCoordinateString()).isEqualTo("(2.0, 2.0)");
    assertThat(busSegmentAdapted.getStart()).isEqualTo(waypoint.getCoordinateString());
    assertThat(busSegment.getTo().getCoordinateString()).isEqualTo("(3.0, 3.0)");
    assertThat(busSegmentAdapted.getEnd()).isEqualTo(busSegment.getTo().getCoordinateString());
    assertThat(busSegmentAdapted.getModes()).isNotNull();
    assertThat(busSegmentAdapted.getModes()).hasSize(1);
    assertThat(busSegmentAdapted.getModes().get(0)).isEqualTo(busSegment.getTransportModeId());
    assertThat(busSegmentAdapted.getStartTime()).isEqualTo((int) busSegment.getStartTimeInSecs());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    assertThat(taxiSegment.getFrom().getCoordinateString()).isEqualTo("(3.0, 3.0)");
    assertThat(taxiSegmentAdapted.getStart()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    assertThat(taxiSegment.getTo().getCoordinateString()).isEqualTo("(4.0, 4.0)");
    assertThat(taxiSegmentAdapted.getEnd()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    assertThat(taxiSegmentAdapted.getModes()).isNotNull();
    assertThat(taxiSegmentAdapted.getModes()).hasSize(1);
    assertThat(taxiSegmentAdapted.getModes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

  }

  @Test public void shouldAdaptSegmentListStopGetOn2ndSegment() {

    ArrayList<TripSegment> segments = new ArrayList<>();
    TripSegment busSegment = new TripSegment();
    busSegment.setId(1L);
    busSegment.setType(SegmentType.SCHEDULED);
    busSegment.setServiceTripId("AwesomeSydney");
    busSegment.setStartStopCode("A");
    busSegment.setEndStopCode("B");
    busSegment.setFrom(new Location(2.0, 2.0));
    busSegment.setTo(new Location(4.0, 4.0));
    busSegment.setTransportModeId("cool_bus");
    busSegment.setStartTimeInSecs(47L);
    busSegment.setEndTimeInSecs(74L);

    TripSegment taxiSegment = new TripSegment();
    taxiSegment.setId(2);
    taxiSegment.setType(SegmentType.UNSCHEDULED);
    taxiSegment.setFrom(new Location(1.0, 1.0));
    taxiSegment.setTo(new Location(2.0, 2.0));
    taxiSegment.setTransportModeId("cool_taxi");
    taxiSegment.setStartTimeInSecs(74);
    taxiSegment.setEndTimeInSecs(80);

    segments.add(taxiSegment);
    segments.add(busSegment);

    ServiceStop waypoint = new ServiceStop(new Location(3.0, 3.0));
    waypoint.setName("SydneyOperaHouse");

    // Taxi 1,1 to 2,2 + Bus 2,2 to 4,4.
    // Get on 2nd segment (Bus), in 3,3.
    // Result shoud be: Taxi 1,1 to 3,3 + Bus 3,3 to 4,4.
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList$production_sources_for_module_waypoints(
        busSegment, waypoint, true, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(0);
    assertThat(taxiSegment.getFrom().getCoordinateString()).isEqualTo("(1.0, 1.0)");
    assertThat(taxiSegmentAdapted.getStart()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    assertThat(waypoint.getCoordinateString()).isEqualTo("(3.0, 3.0)");
    assertThat(taxiSegmentAdapted.getEnd()).isEqualTo(waypoint.getCoordinateString());
    assertThat(taxiSegmentAdapted.getModes()).isNotNull();
    assertThat(taxiSegmentAdapted.getModes()).hasSize(1);
    assertThat(taxiSegmentAdapted.getModes().get(0)).isEqualTo(taxiSegment.getTransportModeId());
    assertThat(taxiSegmentAdapted.getStartTime()).isEqualTo((int) taxiSegment.getStartTimeInSecs());

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(1);
    assertThat(waypoint.getCoordinateString()).isEqualTo("(3.0, 3.0)");
    assertThat(busSegmentAdapted.getStart()).isEqualTo(waypoint.getCoordinateString());
    assertThat(busSegment.getTo().getCoordinateString()).isEqualTo("(4.0, 4.0)");
    assertThat(busSegmentAdapted.getEnd()).isEqualTo(busSegment.getTo().getCoordinateString());
    assertThat(busSegmentAdapted.getModes()).isNotNull();
    assertThat(busSegmentAdapted.getModes()).hasSize(1);
    assertThat(busSegmentAdapted.getModes().get(0)).isEqualTo(busSegment.getTransportModeId());

  }

  @Test public void shouldAdaptSegmentListStopGetOff1stSegment() {

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

    ServiceStop waypoint = new ServiceStop(new Location(2.0, 2.0));
    waypoint.setName("SydneyOperaHouse");

    // Bus 1,1 to 3,3 + Taxi 3,3 to 4,4.
    // Get off 1st segment (Bus), in 2,2.
    // Result shoud be: Bus 1,1 to 2,2 + Taxi 2,2 to 4,4.
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList$production_sources_for_module_waypoints(
        busSegment, waypoint, false, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    assertThat(busSegment.getFrom().getCoordinateString()).isEqualTo("(1.0, 1.0)");
    assertThat(busSegmentAdapted.getStart()).isEqualTo(busSegment.getFrom().getCoordinateString());
    assertThat(waypoint.getCoordinateString()).isEqualTo("(2.0, 2.0)");
    assertThat(busSegmentAdapted.getEnd()).isEqualTo(waypoint.getCoordinateString());
    assertThat(busSegmentAdapted.getModes()).isNotNull();
    assertThat(busSegmentAdapted.getModes()).hasSize(1);
    assertThat(busSegmentAdapted.getModes().get(0)).isEqualTo(busSegment.getTransportModeId());
    assertThat(busSegmentAdapted.getStartTime()).isEqualTo((int) busSegment.getStartTimeInSecs());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    assertThat(waypoint.getCoordinateString()).isEqualTo("(2.0, 2.0)");
    assertThat(taxiSegmentAdapted.getStart()).isEqualTo(waypoint.getCoordinateString());
    assertThat(taxiSegment.getTo().getCoordinateString()).isEqualTo("(4.0, 4.0)");
    assertThat(taxiSegmentAdapted.getEnd()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    assertThat(taxiSegmentAdapted.getModes()).isNotNull();
    assertThat(taxiSegmentAdapted.getModes()).hasSize(1);
    assertThat(taxiSegmentAdapted.getModes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

  }

  @Test public void shouldAdaptSegmentListStopGetOff2ndSegment() {

    ArrayList<TripSegment> segments = new ArrayList<>();
    TripSegment busSegment = new TripSegment();
    busSegment.setId(1L);
    busSegment.setType(SegmentType.SCHEDULED);
    busSegment.setServiceTripId("AwesomeSydney");
    busSegment.setStartStopCode("A");
    busSegment.setEndStopCode("B");
    busSegment.setFrom(new Location(2.0, 2.0));
    busSegment.setTo(new Location(4.0, 4.0));
    busSegment.setTransportModeId("cool_bus");
    busSegment.setStartTimeInSecs(47L);
    busSegment.setEndTimeInSecs(74L);

    TripSegment taxiSegment = new TripSegment();
    taxiSegment.setId(2);
    taxiSegment.setType(SegmentType.UNSCHEDULED);
    taxiSegment.setFrom(new Location(1.0, 1.0));
    taxiSegment.setTo(new Location(2.0, 2.0));
    taxiSegment.setTransportModeId("cool_taxi");
    taxiSegment.setStartTimeInSecs(74);
    taxiSegment.setEndTimeInSecs(80);

    segments.add(taxiSegment);
    segments.add(busSegment);

    ServiceStop waypoint = new ServiceStop(new Location(3.0, 3.0));
    waypoint.setName("SydneyOperaHouse");

    // Taxi 1,1 to 2,2 + Bus 2,2 to 4,4.
    // Get off 2nd segment (Bus), in 3,3.
    // Result shoud be: Taxi 1,1 to 2,2 + Bus 2,2 to 3,3.
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList$production_sources_for_module_waypoints(
        busSegment, waypoint, false, segments);

    assertThat(adaptedList).isNotNull();
    assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(0);
    assertThat(taxiSegment.getFrom().getCoordinateString()).isEqualTo("(1.0, 1.0)");
    assertThat(taxiSegmentAdapted.getStart()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    assertThat(taxiSegment.getTo().getCoordinateString()).isEqualTo("(2.0, 2.0)");
    assertThat(taxiSegmentAdapted.getEnd()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    assertThat(taxiSegmentAdapted.getModes()).isNotNull();
    assertThat(taxiSegmentAdapted.getModes()).hasSize(1);
    assertThat(taxiSegmentAdapted.getModes().get(0)).isEqualTo(taxiSegment.getTransportModeId());
    assertThat(taxiSegmentAdapted.getStartTime()).isEqualTo((int) taxiSegment.getStartTimeInSecs());

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(1);
    assertThat(busSegment.getFrom().getCoordinateString()).isEqualTo("(2.0, 2.0)");
    assertThat(busSegmentAdapted.getStart()).isEqualTo(busSegment.getFrom().getCoordinateString());
    assertThat(waypoint.getCoordinateString()).isEqualTo("(3.0, 3.0)");
    assertThat(busSegmentAdapted.getEnd()).isEqualTo(waypoint.getCoordinateString());
    assertThat(busSegmentAdapted.getModes()).isNotNull();
    assertThat(busSegmentAdapted.getModes()).hasSize(1);
    assertThat(busSegmentAdapted.getModes().get(0)).isEqualTo(busSegment.getTransportModeId());

  }
}



