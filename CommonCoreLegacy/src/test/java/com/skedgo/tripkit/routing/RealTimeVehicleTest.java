package com.skedgo.tripkit.routing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.tripkit.common.Parcels;
import com.skedgo.tripkit.common.model.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RealTimeVehicleTest {
  @Test public void canBeParcelable() {
    final RealTimeVehicle v = new RealTimeVehicle();
    v.setIcon("uber-uberX");
    v.setOccupancy("EMPTY");

    final RealTimeVehicle actual = RealTimeVehicle.CREATOR.createFromParcel(Parcels.parcel(v));
    assertThat(actual.getIcon()).isEqualTo(v.getIcon());
    assertThat(actual.getOccupancy()).isEqualTo(Occupancy.Empty);
  }

  @Test public void canBeCreatedFromJson() {
    // This json was grabbed from an Uber trip.
    final String json = "{\n" +
        "  \"icon\": \"uber-uberX\",\n" +
        "  \"lastUpdate\": 1474903232,\n" +
        "  \"location\": {\n" +
        "    \"bearing\": -150,\n" +
        "    \"lat\": -33.85946,\n" +
        "    \"lng\": 151.21158\n" +
        "  }\n" +
        "}";

    final RealTimeVehicle uberVehicle = new Gson().fromJson(json, RealTimeVehicle.class);

    assertThat(uberVehicle.getIcon()).isEqualTo("uber-uberX");
    assertThat(uberVehicle.getLastUpdateTime()).isEqualTo(1474903232L);
    final Location location = new Location(-33.85946, 151.21158);
    location.setBearing(-150);
    assertThat(uberVehicle.getLocation()).isEqualTo(location);
  }

  @Test
  public void shouldParseOccupancyInfo() throws Exception {

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("occupancy", "FULL");

    RealTimeVehicle realTimeVehicle = new Gson().fromJson(jsonObject, RealTimeVehicle.class);
    assertThat(realTimeVehicle.getOccupancy()).isEqualTo(Occupancy.Full);
  }

  @Test
  public void shouldParseNullOccupancy() throws Exception {
    JsonObject jsonObject = new JsonObject();
    RealTimeVehicle realTimeVehicle = new Gson().fromJson(jsonObject, RealTimeVehicle.class);
    assertThat(realTimeVehicle.getOccupancy()).isEqualTo(null);
  }
}