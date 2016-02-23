package com.skedgo.android.tripkit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import okhttp3.logging.HttpLoggingInterceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripKitImplTest {
  @Mock Configs configs;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    when(configs.context()).thenReturn(RuntimeEnvironment.application);
    when(configs.regionEligibility()).thenReturn("");
  }

  @Test public void singletons() {
    when(configs.debuggable()).thenReturn(true);
    final TripKitImpl kit = new TripKitImpl(configs);

    assertThat(kit.getRegionService()).isNotNull().isSameAs(kit.getRegionService());
    assertThat(kit.getOkHttpClient()).isNotNull().isSameAs(kit.getOkHttpClient());
    assertThat(kit.getRouteService()).isNotNull().isSameAs(kit.getRouteService());
    assertThat(kit.getRegionDatabaseHelper()).isNotNull().isSameAs(kit.getRegionDatabaseHelper());
    assertThat(kit.getReporter()).isNotNull().isSameAs(kit.getReporter());
    assertThat(kit.getTripUpdater()).isNotNull().isSameAs(kit.getTripUpdater());
  }

  @Test public void loggingLevelIsNone() {
    when(configs.debuggable()).thenReturn(false);
    final TripKitImpl kit = new TripKitImpl(configs);
    assertThat(kit.createHttpLoggingInterceptor().getLevel())
        .isEqualTo(HttpLoggingInterceptor.Level.NONE);
  }

  @Test public void loggingLevelIsBody() {
    when(configs.debuggable()).thenReturn(true);
    final TripKitImpl kit = new TripKitImpl(configs);
    assertThat(kit.createHttpLoggingInterceptor().getLevel())
        .isEqualTo(HttpLoggingInterceptor.Level.BODY);
  }
}