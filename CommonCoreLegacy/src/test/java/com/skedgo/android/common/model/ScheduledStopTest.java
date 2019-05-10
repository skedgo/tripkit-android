package com.skedgo.android.common.model;

import android.os.Parcel;

import com.skedgo.android.common.util.Gsons;

import org.junit.Test;
import org.junit.runner.RunWith;

import skedgo.tripkit.routing.ModeInfo;

import static org.assertj.core.api.Java6Assertions.*;


public class ScheduledStopTest {
  @Test public void parseFromJson() {
    final String json = "{\n" +
        "  \"stopType\": \"train\",\n" +
        "  \"name\": \"Blackheath Station\",\n" +
        "  \"class\": \"StopLocation\",\n" +
        "  \"lng\": 150.2844,\n" +
        "  \"stopCode\": \"AU_NSW_Sydney-278510\",\n" +
        "  \"code\": \"AU_NSW_Sydney-278510\",\n" +
        "  \"modeInfo\": {\n" +
        "    \"alt\": \"train\",\n" +
        "    \"localIcon\": \"train\"\n" +
        "  },\n" +
        "  \"lat\": -33.6336,\n" +
        "  \"popularity\": 3465\n" +
        "}";
    final ScheduledStop stop = Gsons.createForLowercaseEnum().fromJson(json, ScheduledStop.class);
    assertThat(stop).isNotNull();
    assertThat(stop.getModeInfo()).isNotNull();
    assertThat(stop.getModeInfo().getAlternativeText()).isEqualTo("train");
    assertThat(stop.getModeInfo().getLocalIconName()).isEqualTo("train");
  }

  @Test public void parcel() {
    ScheduledStop expected = new ScheduledStop();
    expected.setCode("AU_NSW_Sydney-278510");
    expected.setName("Blackheath Station");
    expected.setLat(-33.6336);
    expected.setLon(150.2844);

    ModeInfo modeInfo = new ModeInfo();
    modeInfo.setAlternativeText("train");
    modeInfo.setLocalIconName("train");
    expected.setModeInfo(modeInfo);

    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    ScheduledStop actual = ScheduledStop.CREATOR.createFromParcel(parcel);

    assertThat(actual.getCode()).isEqualTo(expected.getCode());
    assertThat(actual.getName()).isEqualTo(expected.getName());
    assertThat(actual.getLat()).isEqualTo(expected.getLat());
    assertThat(actual.getLon()).isEqualTo(expected.getLon());
    assertThat(actual.getModeInfo()).isNotNull();
    assertThat(actual.getModeInfo().getAlternativeText())
        .isEqualTo(expected.getModeInfo().getAlternativeText());
    assertThat(actual.getModeInfo().getLocalIconName())
        .isEqualTo(expected.getModeInfo().getLocalIconName());
  }
}