package com.skedgo.android.tripkit;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DefaultTripPreferencesTest {
  private DefaultTripPreferences preferences;

  @Before public void before() {
    preferences = new DefaultTripPreferences(ApplicationProvider.getApplicationContext().getSharedPreferences(
        "SomePreferences",
        Context.MODE_PRIVATE
    ));
  }

  @Test public void storeAndQueryConcessionPricingPreference() {
    assertThat(preferences.isConcessionPricingPreferred()).isFalse();
    preferences.setConcessionPricingPreferred(true);
    assertThat(preferences.isConcessionPricingPreferred()).isTrue();
  }

  @Test public void storeAndQueryWheelchairPreference() {
    assertThat(preferences.isWheelchairPreferred()).isFalse();
    preferences.setWheelchairPreferred(true);
    assertThat(preferences.isWheelchairPreferred()).isTrue();
  }
}