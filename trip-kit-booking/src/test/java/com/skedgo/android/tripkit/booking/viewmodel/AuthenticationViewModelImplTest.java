package com.skedgo.android.tripkit.booking.viewmodel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
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