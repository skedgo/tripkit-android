package com.skedgo.android.tripkit;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DefaultTripPreferencesTest {
  private DefaultTripPreferences preferences;

  @Before public void before() {
    preferences = new DefaultTripPreferences(RuntimeEnvironment.application.getSharedPreferences(
        "SomePreferences",
        Context.MODE_PRIVATE
    ));
  }

  @Test public void storeAndQuery() {
    assertThat(preferences.isConcessionPricingPreferred()).isFalse();
    preferences.setConcessionPricingPreferred(true);
    assertThat(preferences.isConcessionPricingPreferred()).isTrue();
  }
}