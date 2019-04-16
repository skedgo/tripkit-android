package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(TestRunner.class)
public class LocationTest {
  @Test public void defaultIdIsZero() {
    Location location = new Location();
    assertThat(location.getId()).isEqualTo(0L);
  }

  @Test public void notPickNullAddress() {
    Location nullAddressLocation = new Location();
    nullAddressLocation.setAddress(null);
    nullAddressLocation.setName("Saigon, Vietnam");
    assertThat(nullAddressLocation.getDisplayName())
        .isEqualTo(nullAddressLocation.getName());
  }

  @Test public void notPickEmptyAddress() {
    Location emptyAddressLocation = new Location();
    emptyAddressLocation.setAddress("");
    emptyAddressLocation.setName("Saigon, Vietnam");
    assertThat(emptyAddressLocation.getDisplayName())
        .isEqualTo(emptyAddressLocation.getName());

    // Even ignore text having duplicate spaces.
    emptyAddressLocation.setAddress("   ");
    assertThat(emptyAddressLocation.getDisplayName())
        .describedAs("Should ignore address having only spaces")
        .isEqualTo(emptyAddressLocation.getName());
  }

  @Test public void notPickNullName() {
    Location nullNameLocation = new Location();
    nullNameLocation.setAddress(null);
    nullNameLocation.setName(null);
    assertThat(nullNameLocation.getDisplayName())
        .isEqualTo(nullNameLocation.getCoordinateString());
  }

  @Test public void notPickEmptyName() {
    Location emptyNameLocation = new Location();
    emptyNameLocation.setAddress(null);
    emptyNameLocation.setName("");
    assertThat(emptyNameLocation.getDisplayName())
        .isEqualTo(emptyNameLocation.getCoordinateString());

    // Even ignore text having duplicate spaces.
    emptyNameLocation.setName("   ");
    assertThat(emptyNameLocation.getDisplayName())
        .describedAs("Should ignore name having only spaces")
        .isEqualTo(emptyNameLocation.getCoordinateString());
  }

  @Test public void parcel() {
    Location expected = new Location();
    expected.setAddress("fake address");
    expected.setName("fake name");
    expected.setLat(33);
    expected.setLon(151);
    expected.setExact(true);
    expected.setBearing(90);
    expected.setPhoneNumber("555-555");
    expected.setUrl("http://www.web.com");
    expected.setTimeZone("Australia/Sydney");
    expected.setPopularity(100);
    expected.setLocationClass("Location Class");
    expected.setW3w("w3w");
    expected.setW3wInfoURL("w3wInfoUrl");

    Location actual = Location.CREATOR.createFromParcel(Utils.parcel(expected));
    assertThat(actual).isEqualTo(expected);
  }

  @Test public void serializedNames() {
    final Location location = new Location();
    location.setLat(1.0);
    location.setLon(2.0);
    location.setAddress("Some address");
    location.setName("Some name");
    location.setTimeZone("America/New_York");
    location.setPopularity(100);
    location.setLocationClass("stop");
    location.setW3w("w3w");
    location.setW3wInfoURL("w3wInfoUrl");

    final JsonObject json = new Gson().toJsonTree(location).getAsJsonObject();
    assertThat(json.has("lat")).isTrue();
    assertThat(json.has("lng")).isTrue();
    assertThat(json.has("address")).isTrue();
    assertThat(json.has("name")).isTrue();
    assertThat(json.has("timezone")).isTrue();
    assertThat(json.has("popularity")).isTrue();
    assertThat(json.has("class")).isTrue();
    assertThat(json.has("w3w")).isTrue();
    assertThat(json.has("w3wInfoURL")).isTrue();
  }

  @Test public void SameLocationsEqual() {
    final Location location = new Location();
    assertThat(location).isEqualTo(location);
  }

  @Test public void equal() {
    final Location a = new Location();
    a.setAddress("Some address");
    a.setName("Some name");
    a.setLat(33);
    a.setLon(151);
    a.setExact(true);
    a.setBearing(90);
    a.setPhoneNumber("555-555");
    a.setUrl("http://www.web.com");
    a.setTimeZone("Australia/Sydney");
    a.setPopularity(100);
    a.setLocationClass("Some class");
    a.setW3w("w3w");
    a.setW3wInfoURL("w3wInfoUrl");

    final Location b = new Location();
    b.setAddress("Some address");
    b.setName("Some name");
    b.setLat(33);
    b.setLon(151);
    b.setExact(true);
    b.setBearing(90);
    b.setPhoneNumber("555-555");
    b.setUrl("http://www.web.com");
    b.setTimeZone("Australia/Sydney");
    b.setPopularity(100);
    b.setLocationClass("Some class");
    b.setW3w("w3w");
    b.setW3wInfoURL("w3wInfoUrl");

    assertThat(a).isEqualTo(b);
  }
}