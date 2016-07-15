package com.skedgo.android.tripkit;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.RealTimeVehicle;
import com.skedgo.android.common.model.Shape;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceResponseTest extends AndroidTestCase {
  public void testSerialize() throws IOException {
    final InputStream stream = getContext().getAssets().open("ServiceResponseTest.json");
    final ServiceResponse response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        ServiceResponse.class
    );

    assertThat(response).isNotNull();
    assertThat(response.type()).isEqualTo("bus");
    assertThat(response.realTimeStatus()).isEqualTo("CAPABLE");
    assertThat(response.shapes())
        .isNotNull()
        .hasSize(1)
        .doesNotContainNull();
    final Shape firstShape = response.shapes().get(0);
    assertThat(firstShape.getStops())
        .isNotNull()
        .hasSize(26);
  }

  public void testUpdatedExample() throws IOException {
    final InputStream stream = getContext().getAssets().open("ServiceResponseTestStops.json");
    final ServiceResponse response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        ServiceResponse.class
    );

    assertThat(response).isNotNull();
    assertThat(response.type()).isEqualTo("bus");
    assertThat(response.realTimeStatus()).isEqualTo("CAPABLE");
    assertThat(response.shapes())
        .isNotNull()
        .hasSize(1)
        .doesNotContainNull();
    final Shape firstShape = response.shapes().get(0);
    assertThat(firstShape.getStops())
        .isNotNull()
        .hasSize(58);
  }

  public void testRealTimeVehicle() throws IOException {
    final InputStream stream = getContext().getAssets().open("ServiceResponseTestRealTime.json");
    final ServiceResponse response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        ServiceResponse.class
    );

    assertThat(response).isNotNull();
    assertThat(response.type()).isEqualTo("bus");
    assertThat(response.realTimeStatus()).isEqualTo("IS_REAL_TIME");
    assertThat(response.shapes())
        .isNotNull()
        .hasSize(1)
        .doesNotContainNull();
    final Shape firstShape = response.shapes().get(0);
    assertThat(firstShape.getStops())
        .isNotNull()
        .hasSize(41);

    RealTimeVehicle vehicle = response.realtimeVehicle();

    assertThat(vehicle).isNotNull();
    assertThat(vehicle.getLastUpdateTime()).isEqualTo(1468594846);
    assertThat(vehicle.getLocation()).isNotNull();

    Location location = vehicle.getLocation();

    assertThat(location.getLat()).isEqualTo(-33.86224);
    assertThat(location.getLon()).isEqualTo(151.21207);
    assertThat(location.getBearing()).isEqualTo(6);

    assertThat(response.realtimeAlternativeVehicle())
        .isNotNull()
        .hasSize(1)
        .doesNotContainNull();

  }
}