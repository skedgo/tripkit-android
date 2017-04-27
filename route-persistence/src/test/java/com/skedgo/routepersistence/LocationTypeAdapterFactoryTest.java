package com.skedgo.routepersistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skedgo.android.common.model.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class LocationTypeAdapterFactoryTest {
  @Test public void shouldOnlyWriteNecessaryProperties() {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new LocationTypeAdapterFactory())
        .create();

    final Location location = new Location(1.0, 2.0);
    location.setTimeZone("Some timezone");
    location.setAddress("Some address");
    location.setName("Some name");
    location.isFavourite(true);
    location.setBearing(20);
    location.setW3wInfoURL("Some w3w info");

    final JsonObject json = gson.toJsonTree(location, Location.class)
        .getAsJsonObject();
    assertThat(json.entrySet()).hasSize(6);
    assertThat(json.has("lat")).isTrue();
    assertThat(json.has("lng")).isTrue();
    assertThat(json.has("name")).isTrue();
    assertThat(json.has("address")).isTrue();
    assertThat(json.has("timezone")).isTrue();
    assertThat(json.has("bearing")).isTrue();
  }
}
