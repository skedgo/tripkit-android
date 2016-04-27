package com.skedgo.android.tripkit.account.model;

import com.skedgo.android.tripkit.account.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SignUpResponseTest {
  @Test public void changedIsFalseByDefault() {
    final SignUpResponse response = ImmutableSignUpResponse.builder()
        .userToken("Some token")
        .build();
    assertThat(response.changed()).isFalse();
  }

  @Test public void userTokenIsMandatory() {
    ImmutableSignUpResponse.builder().build();
  }
}