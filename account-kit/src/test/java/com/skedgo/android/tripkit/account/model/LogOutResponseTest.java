package com.skedgo.android.tripkit.account.model;

import com.skedgo.android.tripkit.account.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LogOutResponseTest {
  @Test public void changedIsFalseByDefault() {
    final LogOutResponse response = ImmutableLogOutResponse.builder().build();
    assertThat(response.changed()).isFalse();
  }
}