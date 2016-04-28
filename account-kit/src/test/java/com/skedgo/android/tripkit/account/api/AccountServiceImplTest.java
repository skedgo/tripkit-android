package com.skedgo.android.tripkit.account.api;

import com.skedgo.android.tripkit.account.BuildConfig;
import com.skedgo.android.tripkit.account.model.ImmutableLogInResponse;
import com.skedgo.android.tripkit.account.model.ImmutableSignUpResponse;
import com.skedgo.android.tripkit.account.model.LogInBody;
import com.skedgo.android.tripkit.account.model.LogInResponse;
import com.skedgo.android.tripkit.account.model.LogOutResponse;
import com.skedgo.android.tripkit.account.model.SignUpBody;
import com.skedgo.android.tripkit.account.model.SignUpResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountServiceImplTest {
  @Mock AccountApi api;
  @Mock UserTokenStore tokenStore;
  private AccountServiceImpl service;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    service = new AccountServiceImpl(api, tokenStore);
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

  @Test public void delegateApiForLoggingOut() {
    when(api.logOutAsync()).thenReturn(Observable.<LogOutResponse>empty());
    service.logOutAsync().subscribe();
    verify(api).logOutAsync();
  }
}