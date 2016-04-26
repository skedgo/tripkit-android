package com.skedgo.android.accountkit.model;

import com.skedgo.android.accountkit.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LogInResponseTest {
  @Test public void changedIsFalseByDefault() {
    final LogInResponse response = ImmutableLogInResponse.builder()
        .userToken("Some token")
        .build();
    assertThat(response.changed()).isFalse();
  }

  @Test public void userTokenIsMandatory() {
    ImmutableLogInResponse.builder().build();
  }
}