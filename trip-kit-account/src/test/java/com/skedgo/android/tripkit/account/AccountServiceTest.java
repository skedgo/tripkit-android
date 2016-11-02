package com.skedgo.android.tripkit.account;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class AccountServiceTest {
  @Mock AccountApi api;
  @Mock UserTokenStore tokenStore;
  private AccountService service;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    final SharedPreferences preferences = RuntimeEnvironment.application.getSharedPreferences(
        AccountServiceTest.class.getSimpleName(),
        Context.MODE_PRIVATE
    );
    service = new AccountService(api, tokenStore, preferences);
  }

  @Test public void saveUserTokenAfterSigningUp() {
    final SignUpResponse response = ImmutableSignUpResponse.builder()
        .changed(true)
        .userToken("Some token")
        .build();
    when(api.signUpAsync(any(SignUpBody.class)))
        .thenReturn(Observable.just(response));

    service.signUpAsync(mock(SignUpBody.class)).subscribe();
    verify(api).signUpAsync(any(SignUpBody.class));
    verify(tokenStore).put(eq("Some token"));
  }

  @Test public void saveUserTokenAfterLoggingIn() {
    final LogInResponse response = ImmutableLogInResponse.builder()
        .changed(true)
        .userToken("Some token")
        .build();
    when(api.logInAsync(any(LogInBody.class)))
        .thenReturn(Observable.just(response));

    service.logInAsync(mock(LogInBody.class)).subscribe();
    verify(api).logInAsync(any(LogInBody.class));
    verify(tokenStore).put(eq("Some token"));
  }

  @Test public void clearUserTokenAfterLoggingOut() {
    when(api.logOutAsync()).thenReturn(Observable.<LogOutResponse>empty());

    service.logOutAsync().subscribe();
    verify(api).logOutAsync();
    verify(tokenStore).put(isNull(String.class));
  }

  @Test public void hasNoUser() {
    when(tokenStore.call()).thenReturn(null);
    assertThat(service.hasUser()).isFalse();
  }

  @Test public void hasUser() {
    when(tokenStore.call()).thenReturn("Some token");
    assertThat(service.hasUser()).isTrue();
  }

  @Test public void shouldInvokeApiWithCorrectTokenEndpoint() {
    when(api.logInSilentAsync(eq("account/android/25251325")))
        .thenReturn(Observable.<LogInResponse>empty());

    service.logInSilentAsync("25251325").subscribe();

    // There should be no slash `/` before `account`.
    // Otherwise, the last path from the base url will be removed.
    verify(api).logInSilentAsync(eq("account/android/25251325"));
  }
}