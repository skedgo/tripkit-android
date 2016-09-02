package com.skedgo.android.common.model;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.skedgo.android.common.util.FileUtils;
import com.skedgo.android.common.util.Gsons;

import org.assertj.core.api.Condition;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutingResponseTest extends AndroidTestCase {
  public void testProcessDirectionTemplate() {
    // Case 1
    JsonPrimitive serviceDirectionNode = new JsonPrimitive("Service direction");
    String notes = "This is a <DIRECTION>";
    String result = RoutingResponse.processDirectionTemplate(serviceDirectionNode, notes);
    assertEquals("This is a Direction: Service direction", result);

    // Case 2
    serviceDirectionNode = new JsonPrimitive("");
    notes = "This is a <DIRECTION>";
    result = RoutingResponse.processDirectionTemplate(serviceDirectionNode, notes);
    assertEquals("This is a ", result);

    // Case 3
    notes = "This is a <DIRECTION>";
    result = RoutingResponse.processDirectionTemplate(null, notes);
    assertEquals("This is a ", result);

    // Case 4
    result = RoutingResponse.processDirectionTemplate(null, null);
    assertNull(result);
    result = RoutingResponse.processDirectionTemplate(null, "");
    assertEquals("", result);
  }

  public void testTripHasReferenceToGroup() throws IOException {
    InputStream routingJsonStream = getContext().getAssets().open("routing0.json");
    String routingJson = FileUtils.readFileStreamAsText(routingJsonStream);

    Gson gson = Gsons.createForLowercaseEnum();
    RoutingResponse response = gson.fromJson(routingJson, RoutingResponse.class);
    response.processRawData(getContext().getResources(), gson);

    for (final TripGroup group : response.getTripGroupList()) {
      assertThat(group.getTrips())
          .describedAs("Trip must have reference to its group")
          .are(new Condition<Trip>() {
            @Override
            public boolean matches(Trip value) {
              return value.getGroup() == group;
            }
          });
    }
  }

  public void testShouldParseProperly() throws IOException {
    InputStream routingJsonStream = getContext().getAssets().open("routing0.json");
    String routingJson = FileUtils.readFileStreamAsText(routingJsonStream);

    Gson gson = Gsons.createForLowercaseEnum();
    RoutingResponse response = gson.fromJson(routingJson, RoutingResponse.class);
    response.processRawData(getContext().getResources(), gson);

    assertThat(response).isNotNull();
    assertThat(response.getAlerts()).hasSize(1);
    assertThat(response.getTripGroupList()).hasSize(1).doesNotContainNull();

    TripGroup group = response.getTripGroupList().get(0);
    assertThat(group.getTrips()).hasSize(1).doesNotContainNull();

    Trip trip = group.getTrips().get(0);
    assertThat(trip.getSegments()).hasSize(3).doesNotContainNull();

    TripSegment motorbikeSegment = trip.getSegments().get(1);
    assertThat(motorbikeSegment.getAlerts()).hasSize(1).doesNotContainNull();

    RealtimeAlert alert = motorbikeSegment.getAlerts().get(0);
    assertThat(alert.severity()).isEqualTo(RealtimeAlert.SEVERITY_WARNING);
    assertThat(alert.title()).isEqualTo("Traffic delay");
    assertThat(RealtimeAlerts.getDisplayText(alert)).isEqualTo("Unusually high traffic on the route.");
  }

  public void testParseMultipleStreets() throws IOException {
    InputStream routingJsonStream = getContext().getAssets().open("routingStreets.json");
    String routingJson = FileUtils.readFileStreamAsText(routingJsonStream);

    Gson gson = Gsons.createForLowercaseEnum();
    RoutingResponse response = gson.fromJson(routingJson, RoutingResponse.class);
    response.processRawData(getContext().getResources(), gson);

    assertThat(response).isNotNull();
    assertThat(response.getTripGroupList()).hasSize(1).doesNotContainNull();

    TripGroup group = response.getTripGroupList().get(0);
    assertThat(group.getTrips()).hasSize(1).doesNotContainNull();

    Trip trip = group.getTrips().get(0);
    assertThat(trip.getSegments()).hasSize(3).doesNotContainNull();

    TripSegment motorbikeSegment = trip.getSegments().get(1);
    assertThat(motorbikeSegment.getStreets()).hasSize(4).doesNotContainNull();

  }
}