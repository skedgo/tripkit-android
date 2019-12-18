package com.skedgo.tripkit;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.skedgo.tripkit.common.model.RealtimeAlert;
import com.skedgo.tripkit.common.model.RealtimeAlerts;
import com.skedgo.tripkit.common.util.Gsons;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import androidx.test.core.app.ApplicationProvider;
import okhttp3.mockwebserver.MockResponse;
import com.skedgo.tripkit.routing.RoutingResponse;
import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;
import com.skedgo.tripkit.routing.TripSegment;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static thuytrinh.mockwebserverrule.MockWebServerRule.createMockResponse;

@RunWith(RobolectricTestRunner.class)
public class RoutingResponseTest {
  @Test public void processDirectionTemplate() {
    // Case 1
    JsonPrimitive serviceDirectionNode = new JsonPrimitive("Service direction");
    String notes = "This is a <DIRECTION>";
    String result = RoutingResponse.processDirectionTemplate(serviceDirectionNode, notes);
    assertThat("This is a Direction: Service direction").isEqualTo(result);

    // Case 2
    serviceDirectionNode = new JsonPrimitive("");
    notes = "This is a <DIRECTION>";
    result = RoutingResponse.processDirectionTemplate(serviceDirectionNode, notes);
    assertThat("This is a ").isEqualTo(result);

    // Case 3
    notes = "This is a <DIRECTION>";
    result = RoutingResponse.processDirectionTemplate(null, notes);
    assertThat("This is a ").isEqualTo(result);

    // Case 4
    result = RoutingResponse.processDirectionTemplate(null, null);
    assertThat(result).isNull();
    result = RoutingResponse.processDirectionTemplate(null, "");
    assertThat("").isEqualTo(result);
  }

  @Test public void tripHasReferenceToGroup() throws IOException {
    MockResponse mockResponse = createMockResponse("/routing0.json");
    String routingJson = mockResponse.getBody().readUtf8();

    Gson gson = Gsons.createForLowercaseEnum();
    RoutingResponse response = gson.fromJson(routingJson, RoutingResponse.class);
    response.processRawData(ApplicationProvider.getApplicationContext().getResources(), gson);

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

  @Test public void shouldParseProperly() throws IOException {
    MockResponse mockResponse = createMockResponse("/routing0.json");
    String routingJson = mockResponse.getBody().readUtf8();

    Gson gson = Gsons.createForLowercaseEnum();
    RoutingResponse response = gson.fromJson(routingJson, RoutingResponse.class);
    response.processRawData(ApplicationProvider.getApplicationContext().getResources(), gson);

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

  @Test public void parseMultipleStreets() throws IOException {
    MockResponse mockResponse = createMockResponse("/routingStreets.json");
    String routingJson = mockResponse.getBody().readUtf8();

    Gson gson = Gsons.createForLowercaseEnum();
    RoutingResponse response = gson.fromJson(routingJson, RoutingResponse.class);
    response.processRawData(ApplicationProvider.getApplicationContext().getResources(), gson);

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