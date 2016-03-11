package com.skedgo.android.tripkit;

import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RegionInfoResponseTest {
  @Test public void jsonProperties() {
    final RegionInfoResponse response = ImmutableRegionInfoResponse.builder()
        .regions(new ArrayList<RegionInfo>())
        .build();
    final JsonObject json = GsonProvider.get().toJsonTree(response).getAsJsonObject();
    assertThat(json.has("regions")).isTrue();
  }
}