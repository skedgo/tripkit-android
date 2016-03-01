package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.TimeTag;
import com.skedgo.android.common.model.TripGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RouteServiceImplTest2 {
  @Mock Resources resources;
  @Mock Func1<String, RoutingApi> routingApiFactory;
  @Mock Func1<Query, Observable<List<Query>>> queryGenerator;
  private RouteServiceImpl routeService;
  private String appVersion = "v1.0";

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    routeService = new RouteServiceImpl(
        resources,
        appVersion,
        queryGenerator,
        routingApiFactory
    );
  }

  @Test public void shouldIncludeSomeOptions() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForArriveBy(25251325));
    query.setIsInterRegional(false);

    final Map<String, String> options = routeService.toOptions(query);
    assertThat(options)
        .containsEntry("version", appVersion)
        .containsEntry("v", "11")
        .containsEntry("unit", query.getUnit())
        .containsEntry("from", "(1.0,2.0)")
        .containsEntry("to", "(3.0,4.0)")
        .containsEntry("arriveBefore", "25251325")
        .containsEntry("departAfter", "0")
        .containsEntry("tt", "2")
        .containsEntry("ws", "4")
        .doesNotContainKey("ir");
  }

  @Test public void shouldIncludeOptionDepartAfter() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForLeaveAfter(25251325));
    query.setIsInterRegional(false);

    final Map<String, String> options = routeService.toOptions(query);
    assertThat(options)
        .containsEntry("arriveBefore", "0")
        .containsEntry("departAfter", "25251325");
  }

  @Test public void shouldContainOptionInterRegional() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForLeaveAfter(25251325));
    query.setIsInterRegional(true);

    final Map<String, String> options = routeService.toOptions(query);
    assertThat(options).containsEntry("ir", "1");
  }

  @Test public void shouldFailSilentlyIfMissingUrls() {
    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, String>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void shouldFailSilentlyIfAllRequestsFail() {
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(anyListOf(String.class), anyMapOf(String.class, String.class)))
        .thenThrow(new RuntimeException());
    when(routingApiFactory.call(anyString()))
        .thenReturn(api);

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com/", "https://www.def.com/"),
        Arrays.asList("hyperloop", "drone"),
        Collections.<String, String>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void shouldFailSilentlyIfNoTripGroupsFoundOnAllUrls() {
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(anyListOf(String.class), anyMapOf(String.class, String.class)))
        .thenReturn(new RoutingResponse());
    when(routingApiFactory.call(anyString()))
        .thenReturn(api);

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com/", "https://www.def.com/"),
        Arrays.asList("hyperloop", "drone"),
        Collections.<String, String>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void emitNothingIfHavingErrorAndNotUserError() {
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some error");
    when(response.hasError()).thenReturn(false);
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(anyList(), anyMap())).thenReturn(response);
    when(routingApiFactory.call(anyString())).thenReturn(api);

    final TestSubscriber<RoutingResponse> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesPerUrlAsync(
        "Some url",
        Collections.<String>emptyList(),
        Collections.<String, String>emptyMap()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }

  @Test public void throwUserError() {
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some user error");
    when(response.hasError()).thenReturn(true);
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(anyList(), anyMap())).thenReturn(response);
    when(routingApiFactory.call(anyString())).thenReturn(api);

    final TestSubscriber<RoutingResponse> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesPerUrlAsync(
        "Some url",
        Collections.<String>emptyList(),
        Collections.<String, String>emptyMap()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(RoutingUserError.class);
    subscriber.assertNoValues();
  }

  @NonNull Query createQuery() {
    final Query query = new Query();
    query.setFromLocation(new Location(1.0, 2.0));
    query.setToLocation(new Location(3.0, 4.0));
    query.setTransferTime(2);
    query.setWalkingSpeed(4);
    query.setUnit("mi");
    return query;
  }
}