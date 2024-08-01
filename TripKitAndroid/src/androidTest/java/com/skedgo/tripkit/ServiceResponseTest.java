package com.skedgo.tripkit;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.skedgo.tripkit.common.model.Location;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.runner.RunWith;
import com.skedgo.tripkit.routing.RealTimeVehicle;
import com.skedgo.tripkit.routing.Shape;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class ServiceResponseTest  {
  private Context context;

  @Before
  public void setUp() {
    // InstrumentationRegistry will do the trick!
    // We can use this context to test implementations of SQLiteOpenHelper.
    context = InstrumentationRegistry.getInstrumentation().getTargetContext();
  }


  public void testSerialize() throws IOException {
    final InputStream stream = context.getAssets().open("ServiceResponseTest.json");
    final ServiceResponse response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        ServiceResponse.class
    );

    assertThat(response).isNotNull();
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
    final InputStream stream = context.getAssets().open("ServiceResponseTestStops.json");
    final ServiceResponse response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        ServiceResponse.class
    );

    assertThat(response).isNotNull();
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
    final InputStream stream = context.getAssets().open("ServiceResponseTestRealTime.json");
    final ServiceResponse response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        ServiceResponse.class
    );

    assertThat(response).isNotNull();
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