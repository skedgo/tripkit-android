package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.Parcels;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class RealTimeVehicleTest {
  @Test public void canBeParcelable() {
    final RealTimeVehicle v = new RealTimeVehicle();
    v.setIcon("uber-uberX");

    final RealTimeVehicle actual = RealTimeVehicle.CREATOR.createFromParcel(Parcels.parcel(v));
    assertThat(actual.getIcon()).isEqualTo(v.getIcon());
  }

  @Test public void canBeCreatedFromJson() {
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
}