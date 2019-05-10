package com.skedgo.android.common.model;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skedgo.android.common.Parcels;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.skedgo.android.common.model.RealtimeAlert.SEVERITY_WARNING;
import static org.assertj.core.api.Java6Assertions.assertThat;


public class RealtimeAlertTest {
  @Test public void jsonKeys() {
    final RealtimeAlert alert = ImmutableRealtimeAlert.builder()
        .title("Some title")
        .text("Some text")
        .stopCode("Some code")
        .serviceTripID("Some id")
        .severity(RealtimeAlert.SEVERITY_ALERT)
        .location(new Location(1.0, 2.0))
        .remoteHashCode(25251325)
        .url("Some url")
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
    assertThat(json.has("url")).isTrue();
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
        .url("Some url")
        .build();

    RealtimeAlert actual = RealtimeAlert.CREATOR.createFromParcel(Parcels.parcel(expected));
    assertThat(actual).isEqualTo(expected);
  }

  @Test public void shouldParseRemoteIconFromJson() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("remoteIcon", "wheelchair-bad_ramp");
    jsonObject.addProperty("hashCode", 100); //required property for RealtimeAlert
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersRealtimeAlert()).create();
    RealtimeAlert realtimeAlert = gson.fromJson(jsonObject, RealtimeAlert.class);
    assertThat(realtimeAlert.remoteIcon()).isEqualTo("wheelchair-bad_ramp");
  }

  public void shouldParcelRemoteIcon() throws Exception {
    RealtimeAlert realtimeAlert = ImmutableRealtimeAlert.builder()
        .remoteHashCode(10L)
        .severity(SEVERITY_WARNING)
        .remoteIcon("hello")
        .build();
    Parcel parcel = Parcels.parcel(realtimeAlert);
    RealtimeAlert actual = RealtimeAlert.CREATOR.createFromParcel(parcel);
    assertThat(actual.remoteIcon()).isEqualTo("hello");
  }
}