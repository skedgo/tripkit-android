package com.skedgo.android.tripkit.booking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class ExternalOAuthServiceTest {

  @Mock private ExternalOAuthStore externalOAuthStore;
  @Mock private ExternalOAuthServiceGenerator externalOAuthServiceGenerator;
  private ExternalOAuthService service;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    service = new ExternalOAuthServiceImpl(externalOAuthStore, externalOAuthServiceGenerator);
  }

  @Test public void shouldGetAccessToken() {

    BookingForm form = mock(BookingForm.class);
    when(form.getClientID()).thenReturn("clientId");
    when(form.getClientSecret()).thenReturn("clientSecret");
    when(form.getTokenURL()).thenReturn("baseUrl");
    when(form.getScope()).thenReturn("scope");
    when(form.getValue()).thenReturn("uber");

    AccessTokenResponse accessTokenResponse = mock(AccessTokenResponse.class);
    when(accessTokenResponse.accessToken()).thenReturn("accessToken");
    when(accessTokenResponse.refreshToken()).thenReturn("refreshToken");
    when(accessTokenResponse.expiresIn()).thenReturn(0L);

    ExternalOAuthApi externalOAuthApi = mock(ExternalOAuthApi.class);
    when(externalOAuthApi.getAccessToken("clientSecret", "clientId", "code",
                                         "authorization_code", "callback", "scope"
    ))
        .thenReturn(Observable.just(accessTokenResponse));

    when(externalOAuthServiceGenerator.createService("baseUrl", "clientId",
                                                     "clientSecret", false
    ))
        .thenReturn(externalOAuthApi);

    ExternalOAuth externalOAuth = mock(ExternalOAuth.class);

    when(externalOAuthStore.updateExternalOauth(any(ExternalOAuth.class)))
        .thenReturn(Observable.just(externalOAuth));

    final TestSubscriber<ExternalOAuth> subscriber = new TestSubscriber<>();

    service.getAccessToken(form, "code", "accessToken", "callback").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    ExternalOAuth result = subscriber.getOnNextEvents().get(0);

    assertThat(externalOAuth).isEqualTo(result);

  }

}
