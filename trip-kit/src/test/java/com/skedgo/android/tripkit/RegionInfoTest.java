package com.skedgo.android.tripkit;

import com.google.gson.JsonObject;
import com.skedgo.android.common.model.ModeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RegionInfoTest {
  @Test public void jsonProperties() {
    final RegionInfo regionInfo = ImmutableRegionInfo.builder()
        .paratransit(new Paratransit())
        .transitModes(Arrays.asList(new ModeInfo(), new ModeInfo()))
        .build();
    final JsonObject json = GsonProvider.get().toJsonTree(regionInfo).getAsJsonObject();
    assertThat(json.has("paratransit")).isTrue();
    assertThat(json.has("transitModes")).isTrue();
  }
}