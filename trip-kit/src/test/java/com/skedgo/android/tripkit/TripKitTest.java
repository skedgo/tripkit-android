package com.skedgo.android.tripkit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripKitTest {
  @Test(expected = IllegalStateException.class)
  public void shouldInitializeTripKitFirst() {
    TripKit.singleton();
  }

  @Test(expected = IllegalStateException.class)
  public void configsShouldNotBeNull() {
    TripKit.initialize(null);
  }
}