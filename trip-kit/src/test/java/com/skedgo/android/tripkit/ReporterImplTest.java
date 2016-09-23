package com.skedgo.android.tripkit;

import com.google.gson.JsonObject;
import com.skedgo.android.common.model.Trip;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ReporterImplTest {
  private static final String PLANNED_URL = "https://granduni.buzzhives.com/satapp/trip/planned/284f2a16-b996-4f06-ba2e-59c91f4543d7";
  @Mock ReportingApi api;
  @Mock Func1<String, ReportingApi> apiFactory;
  @Mock Action1<Throwable> errorHandler;
  private Reporter reporter;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    reporter = new ReporterImpl(apiFactory, errorHandler);
  }

  @Test public void shouldSubscribeErrorHandler() {
    final RuntimeException error = new RuntimeException();
    when(api.reportPlannedTripAsync(eq(Collections.<String, Object>emptyMap())))
        .thenReturn(Observable.<JsonObject>error(error));
    when(apiFactory.call(eq(PLANNED_URL))).thenReturn(api);
    final Trip trip = new Trip();
    trip.setPlannedURL(PLANNED_URL);
    reporter.reportPlannedTrip(trip, null);
    verify(errorHandler, times(1)).call(same(error));
  }

  @Test public void shouldNotRequestApi() {
    reporter.reportPlannedTrip(new Trip(), null);
    verify(api, times(0)).reportPlannedTripAsync(anyMapOf(String.class, Object.class));
  }

  @Test public void shouldUseEmptyMapAsUserInfo() {
    when(api.reportPlannedTripAsync(eq(Collections.<String, Object>emptyMap())))
        .thenReturn(Observable.<JsonObject>empty());
    when(apiFactory.call(eq(PLANNED_URL))).thenReturn(api);
    final Trip trip = new Trip();
    trip.setPlannedURL(PLANNED_URL);
    reporter.reportPlannedTrip(trip, null);
    verify(api, times(1)).reportPlannedTripAsync(eq(Collections.<String, Object>emptyMap()));
  }

  @Test public void shouldAttachUserInfo() {
    final Map<String, Object> userInfo = new HashMap<>();
    userInfo.put("someKey", "someValue");
    when(api.reportPlannedTripAsync(eq(userInfo)))
        .thenReturn(Observable.<JsonObject>empty());
    when(apiFactory.call(eq(PLANNED_URL))).thenReturn(api);
    final Trip trip = new Trip();
    trip.setPlannedURL(PLANNED_URL);
    reporter.reportPlannedTrip(trip, userInfo);
    verify(api, times(1)).reportPlannedTripAsync(eq(userInfo));
  }
}