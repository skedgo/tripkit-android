package com.skedgo.android.accountkit.model;

import com.skedgo.android.accountkit.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SignUpBodyTest {
  @Test public void nameIsOptional() {
    ImmutableSignUpBody.builder()
        .password("Some password")
        .username("Some name")
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void passwordIsMandatory() {
    ImmutableSignUpBody.builder()
        .username("Some name")
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void usernameIsMandatory() {
    ImmutableSignUpBody.builder()
        .password("Some password")
        .build();
  }
}