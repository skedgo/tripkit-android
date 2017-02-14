package com.skedgo.android.tripkit;

import com.skedgo.android.common.util.DiagnosticUtils;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.functions.Func0;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripKitTest {
  @Mock Configs configs;
  @Mock Func0<Func0<String>> baseUrlAdapterFactory;
  private TripKit kit;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    when(configs.context()).thenReturn(RuntimeEnvironment.application);
    when(configs.regionEligibility()).thenReturn("lol");
    kit = DaggerTripKit.builder()
        .mainModule(new MainModule(configs))
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void shouldInitializeTripKitFirst() {
    TripKit.singleton();
  }

  @Test(expected = IllegalStateException.class)
  public void configsShouldNotBeNull() {
    TripKit.initialize(null);
  }

  @Test public void singletons() {
    when(configs.debuggable()).thenReturn(true);

    assertThat(kit.getRegionService()).isNotNull().isSameAs(kit.getRegionService());
    assertThat(kit.getRouteService()).isNotNull().isSameAs(kit.getRouteService());
    assertThat(kit.getRegionDatabaseHelper()).isNotNull().isSameAs(kit.getRegionDatabaseHelper());
    assertThat(kit.getReporter()).isNotNull().isSameAs(kit.getReporter());
    assertThat(kit.getTripUpdater()).isNotNull().isSameAs(kit.getTripUpdater());
  }

  @Test public void nonSingletons() {
    when(configs.debuggable()).thenReturn(true);
    assertThat(kit.getBookingResolver()).isNotNull().isNotSameAs(kit.getBookingResolver());
    assertThat(kit.getLocationInfoService()).isNotNull().isNotSameAs(kit.getLocationInfoService());
  }

  @Test public void hasBaseUrlOverridingInterceptors() {
    when(configs.debuggable()).thenReturn(true);
    when(configs.baseUrlAdapterFactory()).thenReturn(baseUrlAdapterFactory);
    assertThat(kit.getOkHttpClient3().interceptors())
        .hasAtLeastOneElementOfType(BaseUrlOverridingInterceptor.class);
  }

  @Test public void noBaseUrlOverridingInterceptors() {
    when(configs.debuggable()).thenReturn(false);
    when(configs.baseUrlAdapterFactory()).thenReturn(baseUrlAdapterFactory);
    assertThat(kit.getOkHttpClient3().interceptors())
        .doNotHave(new Condition<okhttp3.Interceptor>() {
          @Override public boolean matches(okhttp3.Interceptor value) {
            return value instanceof BaseUrlOverridingInterceptor;
          }
        });
  }

  /* See https://www.flowdock.com/app/skedgo/androiddev/threads/3WbchFGaktP8JunwxJOhtyCw32J. */
  @Test public void headerVersion() {
    // FIXME: Can't mock version name. It's always null in this case.
    final String versionName = DiagnosticUtils.getAppVersionName(RuntimeEnvironment.application);
    final MainModule module = new MainModule(configs);
    assertThat(module.getAppVersion()).isEqualTo("a-lol" + versionName);
  }
}