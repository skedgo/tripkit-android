package com.skedgo.android.tripkit;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(TestRunner.class)
public class DefaultCo2PreferencesTest {
  private DefaultCo2Preferences preferences;

  @Before public void before() {
    preferences = new DefaultCo2Preferences(ApplicationProvider.getApplicationContext().getSharedPreferences(
        "SomePreferences",
        Context.MODE_PRIVATE
    ));
  }

  @Test public void storeAndQueryCo2Profile() {
    preferences.setEmissions("a", 3f);
    preferences.setEmissions("b", 5f);
    preferences.setEmissions("c", 7f);
    assertThat(preferences.getCo2Profile())
        .hasSize(3)
        .containsEntry("a", 3f)
        .containsEntry("b", 5f)
        .containsEntry("c", 7f);
  }
}