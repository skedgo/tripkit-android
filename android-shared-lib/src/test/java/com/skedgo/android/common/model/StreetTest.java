package com.skedgo.android.common.model;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class StreetTest {

  @Test
  public void ShouldBeSafeStreet() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("safe", true);
    Street street = new Gson().fromJson(jsonObject, Street.class);
    assertThat(street.isSafe()).isTrue();
  }

  @Test
  public void ShouldBeUnsafeStreet() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("safe", false);
    Street street = new Gson().fromJson(jsonObject, Street.class);
    assertThat(street.isSafe()).isFalse();
  }

  @Test
  public void shouldParcelSafeStreet() throws Exception {
    Street street = new Street();
    street.setSafe(true);
    Parcel parcel = Utils.parcel(street);
    Street actual = Street.CREATOR.createFromParcel(parcel);
    assertThat(actual.isSafe()).isTrue();
  }

  @Test
  public void shouldParcelUnSafeStreet() throws Exception {
    Street street = new Street();
    street.setSafe(false);
    Parcel parcel = Utils.parcel(street);
    Street actual = Street.CREATOR.createFromParcel(parcel);
    assertThat(actual.isSafe()).isFalse();
  }
}