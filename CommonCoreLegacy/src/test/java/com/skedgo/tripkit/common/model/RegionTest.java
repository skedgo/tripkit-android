package com.skedgo.tripkit.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.tripkit.common.model.region.Region;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class RegionTest {
    @Test
    public void serializedNames() {
        final Region region = new Region();
        region.setName("Mars");
        region.setTimezone("Mars_123");
        region.setURLs(new ArrayList<String>());
        region.setTransportModeIds(new ArrayList<String>());
        region.setEncodedPolyline("_b`|G~j_tL_ry@??_etB~qy@?");
        region.setCities(new ArrayList<Region.City>());

        final JsonObject json = new Gson().toJsonTree(region).getAsJsonObject();
        assertThat(json.has("name")).isTrue();
        assertThat(json.has("urls")).isTrue();
        assertThat(json.has("timezone")).isTrue();
        assertThat(json.has("modes")).isTrue();
        assertThat(json.has("polygon")).isTrue();
        assertThat(json.has("cities")).isTrue();
    }

    @Test
    public void parcel() {
        Region expected = new Region();
        expected.setName("Mars");
        expected.setTimezone("Mars_123");
        expected.setURLs(new ArrayList<>(Arrays.asList("url_0", "url_1")));
        expected.setTransportModeIds(new ArrayList<>(Arrays.asList("m_0", "m_1")));
        expected.setEncodedPolyline("_b`|G~j_tL_ry@??_etB~qy@?");

        final Region.City city0 = new Region.City();
        city0.setName("A");
        final Region.City city1 = new Region.City();
        city1.setName("B");
        expected.setCities(new ArrayList<>(Arrays.asList(
            city0,
            city1
        )));

        Region actual = Region.CREATOR.createFromParcel(Utils.parcel(expected));
        assertNotNull(actual);
        assertThat(actual.getEncodedPolyline()).isEqualTo(expected.getEncodedPolyline());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getTimezone(), actual.getTimezone());
        assertThat(actual.getURLs())
            .describedAs("Parcel urls properly yet?")
            .containsExactlyElementsOf(expected.getURLs());
        assertThat(actual.getTransportModeIds())
            .describedAs("Parcel modes properly yet?")
            .containsExactlyElementsOf(expected.getTransportModeIds());
        assertThat(actual.getCities())
            .describedAs("Parcel cities properly yet?")
            .hasSize(2);
    }
}
