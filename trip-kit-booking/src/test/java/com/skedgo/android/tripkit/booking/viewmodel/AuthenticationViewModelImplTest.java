package com.skedgo.android.tripkit.booking.viewmodel;

import com.skedgo.android.tripkit.booking.BuildConfig;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class AuthenticationViewModelImplTest {
  @Test public void AuthenticationSucceeds() {
    final AuthenticationViewModelImpl viewModel = new AuthenticationViewModelImpl();
    viewModel.verify("www.skedgo.com").toBlocking().single();
    final boolean actual = viewModel.isSuccessful().toBlocking().first();
    assertThat(actual).isTrue();
  }

  @Test public void AuthenticationFails() {
    final AuthenticationViewModelImpl viewModel = new AuthenticationViewModelImpl();
    viewModel.verify("www.google.com").toBlocking().single();
    final boolean actual = viewModel.isSuccessful().toBlocking().first();
    assertThat(actual).isFalse();
  }
}