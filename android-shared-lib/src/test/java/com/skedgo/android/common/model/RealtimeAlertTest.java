package com.skedgo.android.common.model;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.Parcels;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RealtimeAlertTest extends AndroidTestCase {
  @Test public void jsonKeys() {
    final RealtimeAlert alert = ImmutableRealtimeAlert.builder()
        .title("Some title")
        .text("Some text")
        .stopCode("Some code")
        .serviceTripID("Some id")
        .severity(RealtimeAlert.SEVERITY_ALERT)
        .location(new Location(1.0, 2.0))
        .remoteHashCode(25251325)
        .build();

    // Note that toJsonTree(alert) would ignore @JsonAdapter annotation. Kinda weird!
    final JsonObject json = new Gson().toJsonTree(alert, RealtimeAlert.class).getAsJsonObject();
    assertThat(json.has("title")).isTrue();
    assertThat(json.has("text")).isTrue();
    assertThat(json.has("stopCode")).isTrue();
    assertThat(json.has("serviceTripID")).isTrue();
    assertThat(json.has("hashCode")).isTrue();
    assertThat(json.has("severity")).isTrue();
    assertThat(json.has("location")).isTrue();
  }

  @Test public void parcel() {
    final RealtimeAlert expected = ImmutableRealtimeAlert.builder()
        .title("Some title")
        .text("Some text")
        .stopCode("Some code")
        .serviceTripID("Some id")
        .severity(RealtimeAlert.SEVERITY_ALERT)
        .location(new Location(1.0, 2.0))
        .remoteHashCode(25251325)
        .build();

    RealtimeAlert actual = RealtimeAlert.CREATOR.createFromParcel(Parcels.parcel(expected));
    assertThat(actual).isEqualTo(expected);
  }
}