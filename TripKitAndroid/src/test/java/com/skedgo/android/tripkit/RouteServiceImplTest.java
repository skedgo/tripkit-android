package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.TimeTag;
import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider;
import skedgo.tripkit.a2brouting.FailoverRoutingApi;

import org.assertj.core.data.MapEntry;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class RouteServiceImplTest {
  @Mock Func1<Query, Observable<List<Query>>> queryGenerator;
  @Mock ExcludedTransitModesAdapter excludedTransitModesAdapter;
  @Mock Co2Preferences co2Preferences;
  @Mock TripPreferences tripPreferences;
  @Mock ExtraQueryMapProvider extraQueryMapProvider;
  @Mock FailoverRoutingApi routingApi;
  private RouteServiceImpl routeService;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    routeService = new RouteServiceImpl(
        queryGenerator,
        excludedTransitModesAdapter,
        co2Preferences,
        tripPreferences,
        extraQueryMapProvider,
        routingApi
    );
    when(extraQueryMapProvider.call())
        .thenReturn(Collections.<String, Object>emptyMap());
  }

  @Test public void shouldIncludeSomeOptions() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForArriveBy(25251325));
    query.setIsInterRegional(false);

    final Map<String, Object> options = routeService.toOptions(query);
    assertThat(options)
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

  @Test public void shouldIncludeAddressString() {
    final Query query = createQuery();
    query.getFromLocation().setAddress("from address");
    query.getToLocation().setAddress("to address");

    final Map<String, Object> options = routeService.toOptions(query);
    assertThat(options)
        .containsEntry("from", "(1.0,2.0)\"from address\"")
        .containsEntry("to", "(3.0,4.0)\"to address\"");
  }

  /**
   * Given an {@link ExtraQueryMapProvider} that returns an extra query map,
   * we expect that the query map returned by {@link RouteServiceImpl#toOptions(Query)}
   * should contain all the entries from the extra query map.
   */
  @Test public void shouldIncludeExtraQueryMap() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForArriveBy(25251325));
    query.setIsInterRegional(false);

    final Map<String, Object> extraQueryMap = new HashMap<>();
    extraQueryMap.put("bsb", 1);
    when(extraQueryMapProvider.call())
        .thenReturn(extraQueryMap);

    final Map<String, Object> options = routeService.toOptions(query);
    assertThat(options).contains(MapEntry.entry("bsb", 1));
  }

  @Test public void includeConcessionPricing() {
    when(tripPreferences.isConcessionPricingPreferred()).thenReturn(true);
    assertThat(routeService.getParamsByPreferences()).containsEntry("conc", true);
  }

  @Test public void excludeConcessionPricing() {
    when(tripPreferences.isConcessionPricingPreferred()).thenReturn(false);
    assertThat(routeService.getParamsByPreferences()).doesNotContainKey("conc");
  }

  /* See https://redmine.buzzhives.com/issues/7663. */
  @Test public void includeWheelchairInfo() {
    when(tripPreferences.isWheelchairPreferred()).thenReturn(true);
    assertThat(routeService.getParamsByPreferences()).containsEntry("wheelchair", true);
  }

  /* See https://redmine.buzzhives.com/issues/7663. */
  @Test public void excludeWheelchairInfo() {
    when(tripPreferences.isWheelchairPreferred()).thenReturn(false);
    assertThat(routeService.getParamsByPreferences()).doesNotContainKey("wheelchair");
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

  private Query createQuery() {
    final Query query = new Query();
    query.setFromLocation(new Location(1.0, 2.0));
    query.setToLocation(new Location(3.0, 4.0));
    query.setTransferTime(2);
    query.setWalkingSpeed(4);
    query.setUnit("mi");
    return query;
  }
}
