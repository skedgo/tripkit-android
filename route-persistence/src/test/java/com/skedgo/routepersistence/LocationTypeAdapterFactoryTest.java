package com.skedgo.routepersistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skedgo.tripkit.common.model.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocationTypeAdapterFactoryTest {
    @Test
    public void shouldOnlyWriteNecessaryProperties() {
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
