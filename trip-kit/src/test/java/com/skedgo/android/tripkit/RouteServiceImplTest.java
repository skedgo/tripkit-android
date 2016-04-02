package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.TimeTag;
import com.skedgo.android.common.model.TripGroup;

import org.assertj.core.util.Maps;
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
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RouteServiceImplTest {
  @Mock Resources resources;
  @Mock Func1<String, RoutingApi> routingApiFactory;
  @Mock Func1<Query, Observable<List<Query>>> queryGenerator;
  @Mock ExcludedTransitModesAdapter excludedTransitModesAdapter;
  @Mock Co2Preferences co2Preferences;
  @Mock TripPreferences tripPreferences;
  private RouteServiceImpl routeService;
  private String appVersion = "v1.0";

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    routeService = new RouteServiceImpl(
        resources,
        appVersion,
        queryGenerator,
        routingApiFactory,
        excludedTransitModesAdapter,
        co2Preferences,
        tripPreferences,
        new Gson()
    );
  }

  @Test public void shouldIncludeSomeOptions() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForArriveBy(25251325));
    query.setIsInterRegional(false);

    final Map<String, Object> options = routeService.toOptions(query);
    assertThat(options)
        .containsEntry("version", appVersion)
        .containsEntry("v", "12")
        .containsEntry("unit", query.getUnit())
        .containsEntry("from", "(1.0,2.0)")
        .containsEntry("to", "(3.0,4.0)")
        .containsEntry("arriveBefore", "25251325")
        .containsEntry("departAfter", "0")
        .containsEntry("tt", "2")
        .containsEntry("ws", "4")
        .doesNotContainKey("ir");
  }

  @Test public void includeConcessionPricing() {
    when(tripPreferences.isConcessionPricingPreferred()).thenReturn(true);
    assertThat(routeService.getParamsByPreferences()).containsEntry("conc", true);
  }

  @Test public void excludeConcessionPricing() {
    when(tripPreferences.isConcessionPricingPreferred()).thenReturn(false);
    assertThat(routeService.getParamsByPreferences()).doesNotContainKey("conc");
  }

  @Test public void shouldIncludeOptionDepartAfter() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForLeaveAfter(25251325));
    query.setIsInterRegional(false);

    final Map<String, Object> options = routeService.toOptions(query);
    assertThat(options)
        .containsEntry("arriveBefore", "0")
        .containsEntry("departAfter", "25251325");
  }

  @Test public void shouldContainOptionInterRegional() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForLeaveAfter(25251325));
    query.setIsInterRegional(true);

    final Map<String, Object> options = routeService.toOptions(query);
    assertThat(options).containsEntry("ir", "1");
  }

  @Test public void shouldFailSilentlyIfMissingUrls() {
    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void shouldFailSilentlyIfAllRequestsFail() {
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenThrow(new RuntimeException());
    when(routingApiFactory.call(anyString()))
        .thenReturn(api);

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com/", "https://www.def.com/"),
        Arrays.asList("hyperloop", "drone"),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void shouldFailSilentlyIfNoTripGroupsFoundOnAllUrls() {
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(new RoutingResponse());
    when(routingApiFactory.call(anyString()))
        .thenReturn(api);

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com/", "https://www.def.com/"),
        Arrays.asList("hyperloop", "drone"),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
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
    when(api.fetchRoutes(
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(response);
    when(routingApiFactory.call(anyString())).thenReturn(api);

    final TestSubscriber<RoutingResponse> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesPerUrlAsync(
        "Some url",
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }

  @Test public void throwUserError() {
    final String url = "https://skedgo.com/tripgo";
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some user error");
    when(response.hasError()).thenReturn(true);
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(response);
    when(routingApiFactory.call(eq(url))).thenReturn(api);

    final TestSubscriber<RoutingResponse> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesPerUrlAsync(
        url,
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(RoutingUserError.class);
    subscriber.assertNoValues();
  }

  @Test public void throwUserErrorWhenRoutingWithMultipleUrls() {
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some user error");
    when(response.hasError()).thenReturn(true);
    final RoutingApi api = mock(RoutingApi.class);
    when(api.fetchRoutes(
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(response);
    when(routingApiFactory.call(anyString())).thenReturn(api);

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    routeService.fetchRoutesAsync(
        Arrays.asList("a", "b", "c"),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(RoutingUserError.class);
    subscriber.assertNoValues();
  }

  @Test public void getExcludedTransitModesAsNonNull() {
    final String regionName = "Some region name";
    assertThat(routeService.getExcludedTransitModesAsNonNull(
        null,
        regionName
    )).isEmpty();
    assertThat(routeService.getExcludedTransitModesAsNonNull(
        excludedTransitModesAdapter,
        regionName
    )).isEmpty();

    final List<String> excludedTransitModes = Arrays.asList("a", "b", "c");
    when(excludedTransitModesAdapter.call(eq(regionName)))
        .thenReturn(excludedTransitModes);
    assertThat(routeService.getExcludedTransitModesAsNonNull(
        excludedTransitModesAdapter,
        regionName
    )).isSameAs(excludedTransitModes);
  }

  @Test public void includeCo2Profile() {
    final Map<String, Float> co2Profile = Maps.newHashMap();
    co2Profile.put("a", 2f);
    co2Profile.put("b", 5f);
    when(co2Preferences.getCo2Profile()).thenReturn(co2Profile);
    assertThat(routeService.getParamsByPreferences())
        .hasSize(2)
        .containsEntry("co2[a]", 2f)
        .containsEntry("co2[b]", 5f);
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