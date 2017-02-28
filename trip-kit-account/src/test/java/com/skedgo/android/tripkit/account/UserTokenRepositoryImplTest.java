package com.skedgo.android.tripkit.account;

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
public class UserTokenRepositoryImplTest {
  private UserTokenRepositoryImpl userTokenStore;

  @Before public void before() {
    userTokenStore = new UserTokenRepositoryImpl(RuntimeEnvironment.application.getSharedPreferences(
        UserTokenRepositoryImplTest.class.getSimpleName(),
        Context.MODE_PRIVATE
    ));
  }

  @Test public void noDefaultToken() {
    assertThat(userTokenStore.getUserToken()).isNull();
  }

  @Test public void putAndGetToken() {
    final String userToken = "Some token";
    userTokenStore.putUserToken(userToken);
    assertThat(userTokenStore.getUserToken()).isEqualTo(userToken);
  }
}