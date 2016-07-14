package com.skedgo.android.tripkit.booking;

import com.skedgo.android.common.model.Region;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.HttpUrl;
import rx.Observable;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AuthServiceImplTest {
  @Mock AuthApi api;
  private AuthServiceImpl service;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    service = new AuthServiceImpl(api);
  }

  @Test public void delegateFetchingProviders() {
    final Region region = mock(Region.class);
    when(region.getURLs()).thenReturn(new ArrayList<>(
        Collections.singletonList("https://tripgo.skedgo.com/satapp/")
    ));
    when(region.getName()).thenReturn("The_Ark");
    final HttpUrl url = HttpUrl.parse("https://tripgo.skedgo.com/satapp/auth/The_Ark");
    when(api.fetchProvidersAsync(
        url
    )).thenReturn(Observable.just(Collections.<AuthProvider>emptyList()));
    service.fetchProvidersByRegionAsync(region, null).subscribe();
    verify(api).fetchProvidersAsync(eq(url));
  }

  @Test public void delegateSigningIn() {
    service.signInAsync("Some url");
    verify(api).signInAsync(eq("Some url"));
  }

  @Test public void delegateLoggingOut() {
    service.logOutAsync("Some url");
    verify(api).logOutAsync(eq("Some url"));
  }
}