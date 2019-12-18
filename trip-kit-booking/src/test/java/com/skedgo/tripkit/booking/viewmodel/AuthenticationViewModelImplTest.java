package com.skedgo.tripkit.booking.viewmodel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AuthenticationViewModelImplTest {
  @Test public void AuthenticationSucceeds() {
    final AuthenticationViewModelImpl viewModel = new AuthenticationViewModelImpl();
    final boolean actual = viewModel.verify("www.skedgo.com").blockingSingle();
    assertThat(actual).isTrue();
  }

  @Test public void AuthenticationFails() {
    final AuthenticationViewModelImpl viewModel = new AuthenticationViewModelImpl();
    viewModel.verify("www.google.com").blockingSingle();
    final boolean actual = viewModel.isSuccessful().blockingFirst();
    assertThat(actual).isFalse();
  }
}