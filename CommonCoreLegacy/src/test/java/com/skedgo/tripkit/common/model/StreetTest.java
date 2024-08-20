package com.skedgo.tripkit.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class StreetTest {
    @Test
    public void allPropertiesAreOptional() {
        ImmutableStreet.builder().build();
    }

    @Test
    public void shouldBeSafeStreet() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("safe", true);
        Street street = new Gson().fromJson(json, Street.class);
        assertThat(street.safe()).isTrue();
    }

    @Test
    public void shouldBeUnsafeStreet() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("safe", false);
        Street street = new Gson().fromJson(json, Street.class);
        assertThat(street.safe()).isFalse();
    }

    @Test
    public void shouldParcelSafeStreet() throws Exception {
        Street street = ImmutableStreet.builder().safe(true).build();
        Street actual = Street.CREATOR.createFromParcel(Utils.parcel(street));
        assertThat(actual.safe()).isTrue();
    }

    @Test
    public void shouldParcelUnSafeStreet() throws Exception {
        Street street = ImmutableStreet.builder().safe(false).build();
        Street actual = Street.CREATOR.createFromParcel(Utils.parcel(street));
        assertThat(actual.safe()).isFalse();
    }
}
