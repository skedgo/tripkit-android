package com.skedgo.android.tripkit;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UuidProviderTest {
  private UuidProvider provider;
  private SharedPreferences preferences;

  @Before public void before() {
    preferences = RuntimeEnvironment.application.getSharedPreferences(
        "TripKit",
        Context.MODE_PRIVATE
    );
    provider = new UuidProvider(preferences);
  }

  @Test public void generateUuid() {
    final String uuid = provider.call();
    assertThat(uuid).isNotNull().isNotEmpty();

    provider = new UuidProvider(preferences);
    assertThat(provider.call()).isEqualTo(uuid);
  }

  @Test public void usePersistentUuid() {
    preferences.edit()
        .putString("UUID", "Some UUID")
        .apply();
    assertThat(provider.call()).isEqualTo("Some UUID");
  }
}