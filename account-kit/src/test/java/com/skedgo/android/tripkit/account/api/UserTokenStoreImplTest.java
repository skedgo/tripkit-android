package com.skedgo.android.tripkit.account.api;

import android.content.Context;

import com.skedgo.android.tripkit.account.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserTokenStoreImplTest {
  private UserTokenStoreImpl userTokenStore;

  @Before public void before() {
    userTokenStore = new UserTokenStoreImpl(RuntimeEnvironment.application.getSharedPreferences(
        UserTokenStoreImplTest.class.getSimpleName(),
        Context.MODE_PRIVATE
    ));
  }

  @Test public void noDefaultToken() {
    assertThat(userTokenStore.call()).isNull();
  }

  @Test public void putAndGetToken() {
    final String userToken = "Some token";
    userTokenStore.put(userToken);
    assertThat(userTokenStore.call()).isEqualTo(userToken);
  }
}