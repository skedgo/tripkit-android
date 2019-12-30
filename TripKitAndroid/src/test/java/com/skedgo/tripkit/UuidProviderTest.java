package com.skedgo.tripkit;

import android.content.Context;
import android.content.SharedPreferences;

import com.skedgo.tripkit.UuidProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UuidProviderTest {
  private UuidProvider provider;
  private SharedPreferences preferences;

  @Before public void before() {
    preferences = ApplicationProvider.getApplicationContext().getSharedPreferences(
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