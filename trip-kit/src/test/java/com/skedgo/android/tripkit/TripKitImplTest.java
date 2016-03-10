package com.skedgo.android.tripkit;

import com.skedgo.android.common.util.DiagnosticUtils;

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
    when(configs.regionEligibility()).thenReturn("lol");
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

  /* See https://www.flowdock.com/app/skedgo/androiddev/threads/3WbchFGaktP8JunwxJOhtyCw32J. */
  @Test public void headerVersion() {
    // FIXME: Can't mock version name. It's always null in this case.
    final String versionName = DiagnosticUtils.getAppVersionName(RuntimeEnvironment.application);
    final TripKitImpl kit = new TripKitImpl(configs);
    assertThat(kit.getAppVersion()).isEqualTo("a-lol" + versionName);
  }
}