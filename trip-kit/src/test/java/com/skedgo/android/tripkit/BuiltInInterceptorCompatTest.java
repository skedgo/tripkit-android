package com.skedgo.android.tripkit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Locale;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BuiltInInterceptorCompatTest {
  @Test(expected = IllegalStateException.class)
  public void appVersionIsMandatory() {
    BuiltInInterceptorCompatBuilder.create()
        .regionEligibility("Some name")
        .locale(Locale.US)
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void regionEligibilityIsMandatory() {
    BuiltInInterceptorCompatBuilder.create()
        .appVersion("Some version")
        .locale(Locale.US)
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void localeIsMandatory() {
    BuiltInInterceptorCompatBuilder.create()
        .appVersion("Some version")
        .regionEligibility("Some name")
        .build();
  }

  @Test public void userTokenProviderIsOptional() {
    BuiltInInterceptorCompatBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .regionEligibility("Some name")
        .build();
  }
}