package com.skedgo.android.tripkit.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LogInBodyTest {
  @Test(expected = IllegalStateException.class)
  public void passwordIsMandatory() {
    ImmutableLogInBody.builder()
        .username("Some name")
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void usernameIsMandatory() {
    ImmutableLogInBody.builder()
        .password("Some password")
        .build();
  }
}