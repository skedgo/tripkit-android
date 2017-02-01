
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

    TimetableEntry timetableEntry = Mockito.mock(TimetableEntry.class);
    Mockito.when(timetableEntry.getStopCode()).thenReturn("stop code");
    Mockito.when(timetableEntry.getEndStopCode()).thenReturn("end stop code");
    Mockito.when(timetableEntry.getStartTimeInSecs()).thenReturn(10L);
    Mockito.when(timetableEntry.getEndTimeInSecs()).thenReturn(20L);
    Mockito.when(timetableEntry.getOperator()).thenReturn("operator");

    Region region = Mockito.mock(Region.class);
    Mockito.when(region.getName()).thenReturn("region");

    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptServiceSegmentList(
        busSegment, timetableEntry, region, segments);

    Assertions.assertThat(adaptedList).isNotNull();
    Assertions.assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    Assertions.assertThat(busSegmentAdapted.start()).isEqualTo(timetableEntry.getStopCode());
    Assertions.assertThat(busSegmentAdapted.end()).isEqualTo(timetableEntry.getEndStopCode());
    Assertions.assertThat(busSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(busSegmentAdapted.modes()).hasSize(2);
    Assertions.assertThat(busSegmentAdapted.modes().get(0)).isEqualTo("pt_pub");
    Assertions.assertThat(busSegmentAdapted.modes().get(1)).isEqualTo("pt_sch");
    Assertions.assertThat(busSegmentAdapted.startTime()).isEqualTo((int) timetableEntry.getStartTimeInSecs());
    Assertions.assertThat(busSegmentAdapted.endTime()).isEqualTo((int) timetableEntry.getEndTimeInSecs());
    Assertions.assertThat(busSegmentAdapted.region()).isEqualTo(region.getName());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    Assertions.assertThat(taxiSegmentAdapted.start()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    Assertions.assertThat(taxiSegmentAdapted.end()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    Assertions.assertThat(taxiSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

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
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList(
        busSegment, waypoint, true, segments);

    Assertions.assertThat(adaptedList).isNotNull();
    Assertions.assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    Assertions.assertThat(waypoint.getCoordinateString()).isEqualTo("(2.0, 2.0)");
    Assertions.assertThat(busSegmentAdapted.start()).isEqualTo(waypoint.getCoordinateString());
    Assertions.assertThat(busSegment.getTo().getCoordinateString()).isEqualTo("(3.0, 3.0)");
    Assertions.assertThat(busSegmentAdapted.end()).isEqualTo(busSegment.getTo().getCoordinateString());
    Assertions.assertThat(busSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(busSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(busSegmentAdapted.modes().get(0)).isEqualTo(busSegment.getTransportModeId());
    Assertions.assertThat(busSegmentAdapted.startTime()).isEqualTo((int) busSegment.getStartTimeInSecs());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    Assertions.assertThat(taxiSegment.getFrom().getCoordinateString()).isEqualTo("(3.0, 3.0)");
    Assertions.assertThat(taxiSegmentAdapted.start()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    Assertions.assertThat(taxiSegment.getTo().getCoordinateString()).isEqualTo("(4.0, 4.0)");
    Assertions.assertThat(taxiSegmentAdapted.end()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    Assertions.assertThat(taxiSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

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
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList(
        busSegment, waypoint, true, segments);

    Assertions.assertThat(adaptedList).isNotNull();
    Assertions.assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(0);
    Assertions.assertThat(taxiSegment.getFrom().getCoordinateString()).isEqualTo("(1.0, 1.0)");
    Assertions.assertThat(taxiSegmentAdapted.start()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    Assertions.assertThat(waypoint.getCoordinateString()).isEqualTo("(3.0, 3.0)");
    Assertions.assertThat(taxiSegmentAdapted.end()).isEqualTo(waypoint.getCoordinateString());
    Assertions.assertThat(taxiSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());
    Assertions.assertThat(taxiSegmentAdapted.startTime()).isEqualTo((int) taxiSegment.getStartTimeInSecs());

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(1);
    Assertions.assertThat(waypoint.getCoordinateString()).isEqualTo("(3.0, 3.0)");
    Assertions.assertThat(busSegmentAdapted.start()).isEqualTo(waypoint.getCoordinateString());
    Assertions.assertThat(busSegment.getTo().getCoordinateString()).isEqualTo("(4.0, 4.0)");
    Assertions.assertThat(busSegmentAdapted.end()).isEqualTo(busSegment.getTo().getCoordinateString());
    Assertions.assertThat(busSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(busSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(busSegmentAdapted.modes().get(0)).isEqualTo(busSegment.getTransportModeId());

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
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList(
        busSegment, waypoint, false, segments);

    Assertions.assertThat(adaptedList).isNotNull();
    Assertions.assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(0);
    Assertions.assertThat(busSegment.getFrom().getCoordinateString()).isEqualTo("(1.0, 1.0)");
    Assertions.assertThat(busSegmentAdapted.start()).isEqualTo(busSegment.getFrom().getCoordinateString());
    Assertions.assertThat(waypoint.getCoordinateString()).isEqualTo("(2.0, 2.0)");
    Assertions.assertThat(busSegmentAdapted.end()).isEqualTo(waypoint.getCoordinateString());
    Assertions.assertThat(busSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(busSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(busSegmentAdapted.modes().get(0)).isEqualTo(busSegment.getTransportModeId());
    Assertions.assertThat(busSegmentAdapted.startTime()).isEqualTo((int) busSegment.getStartTimeInSecs());

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(1);
    Assertions.assertThat(waypoint.getCoordinateString()).isEqualTo("(2.0, 2.0)");
    Assertions.assertThat(taxiSegmentAdapted.start()).isEqualTo(waypoint.getCoordinateString());
    Assertions.assertThat(taxiSegment.getTo().getCoordinateString()).isEqualTo("(4.0, 4.0)");
    Assertions.assertThat(taxiSegmentAdapted.end()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    Assertions.assertThat(taxiSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());

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
    List<WaypointSegmentAdapter> adaptedList = waypointSegmentAdapterUtils.adaptStopSegmentList(
        busSegment, waypoint, false, segments);

    Assertions.assertThat(adaptedList).isNotNull();
    Assertions.assertThat(adaptedList).hasSize(2);

    WaypointSegmentAdapter taxiSegmentAdapted = adaptedList.get(0);
    Assertions.assertThat(taxiSegment.getFrom().getCoordinateString()).isEqualTo("(1.0, 1.0)");
    Assertions.assertThat(taxiSegmentAdapted.start()).isEqualTo(taxiSegment.getFrom().getCoordinateString());
    Assertions.assertThat(taxiSegment.getTo().getCoordinateString()).isEqualTo("(2.0, 2.0)");
    Assertions.assertThat(taxiSegmentAdapted.end()).isEqualTo(taxiSegment.getTo().getCoordinateString());
    Assertions.assertThat(taxiSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(taxiSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(taxiSegmentAdapted.modes().get(0)).isEqualTo(taxiSegment.getTransportModeId());
    Assertions.assertThat(taxiSegmentAdapted.startTime()).isEqualTo((int) taxiSegment.getStartTimeInSecs());

    WaypointSegmentAdapter busSegmentAdapted = adaptedList.get(1);
    Assertions.assertThat(busSegment.getFrom().getCoordinateString()).isEqualTo("(2.0, 2.0)");
    Assertions.assertThat(busSegmentAdapted.start()).isEqualTo(busSegment.getFrom().getCoordinateString());
    Assertions.assertThat(waypoint.getCoordinateString()).isEqualTo("(3.0, 3.0)");
    Assertions.assertThat(busSegmentAdapted.end()).isEqualTo(waypoint.getCoordinateString());
    Assertions.assertThat(busSegmentAdapted.modes()).isNotNull();
    Assertions.assertThat(busSegmentAdapted.modes()).hasSize(1);
    Assertions.assertThat(busSegmentAdapted.modes().get(0)).isEqualTo(busSegment.getTransportModeId());

  }
}



