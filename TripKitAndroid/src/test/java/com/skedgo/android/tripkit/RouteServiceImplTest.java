package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.TimeTag;
import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider;
import com.skedgo.android.tripkit.tsp.RegionInfo;
import com.skedgo.android.tripkit.tsp.RegionInfoRepository;

import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import skedgo.tripkit.a2brouting.FailoverA2bRoutingApi;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RouteServiceImplTest extends TripKitAndroidRobolectricTest {
  @Mock QueryGenerator queryGenerator;
  @Mock Co2Preferences co2Preferences;
  @Mock TripPreferences tripPreferences;
  @Mock ExtraQueryMapProvider extraQueryMapProvider;
  @Mock FailoverA2bRoutingApi routingApi;
  @Mock RegionInfoRepository regionInfoRepository;
  @Mock RegionInfo regionInfo;
  private RouteServiceImpl routeService;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    routeService = new RouteServiceImpl(
        queryGenerator,
        co2Preferences,
        tripPreferences,
        extraQueryMapProvider,
        routingApi,
        regionInfoRepository
    );
    when(extraQueryMapProvider.call())
        .thenReturn(Collections.<String, Object>emptyMap());
    when(regionInfoRepository.getRegionInfoByRegion(any())).thenReturn(Observable.just(regionInfo));
    when(regionInfo.transitWheelchairAccessibility()).thenReturn(false);
  }

  @Test public void shouldIncludeSomeOptions() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForArriveBy(25251325));
    query.setCyclingSpeed(3);

    final Map<String, Object> options = routeService.toOptions(query, regionInfo);
    assertThat(options)
        .containsEntry("v", "12")
        .containsEntry("unit", query.getUnit())
        .containsEntry("from", "(1.0,2.0)")
        .containsEntry("to", "(3.0,4.0)")
        .containsEntry("arriveBefore", "25251325")
        .containsEntry("departAfter", "0")
        .containsEntry("tt", "2")
        .containsEntry("ws", "4")
        .containsEntry("cs", "3")
        .doesNotContainKey("ir");
  }

  @Test public void shouldIncludeAddressString() {
    final Query query = createQuery();
    query.getFromLocation().setAddress("from address");
    query.getToLocation().setAddress("to address");

    final Map<String, Object> options = routeService.toOptions(query, regionInfo);
    assertThat(options)
        .containsEntry("from", "(1.0,2.0)\"from address\"")
        .containsEntry("to", "(3.0,4.0)\"to address\"");
  }

  /**
   * Given an {@link ExtraQueryMapProvider} that returns an extra query map,
   * we expect that the query map returned by {@link RouteServiceImpl#toOptions(Query, RegionInfo)}
   * should contain all the entries from the extra query map.
   */
  @Test public void shouldIncludeExtraQueryMap() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForArriveBy(25251325));

    final Map<String, Object> extraQueryMap = new HashMap<>();
    extraQueryMap.put("bsb", 1);
    when(extraQueryMapProvider.call())
        .thenReturn(extraQueryMap);

    final Map<String, Object> options = routeService.toOptions(query, regionInfo);
    assertThat(options).contains(MapEntry.entry("bsb", 1));
  }

  @Test public void includeConcessionPricing() {
    when(tripPreferences.isConcessionPricingPreferred()).thenReturn(true);
    assertThat(routeService.getParamsByPreferences(regionInfo)).containsEntry("conc", true);
  }

  @Test public void excludeConcessionPricing() {
    when(tripPreferences.isConcessionPricingPreferred()).thenReturn(false);
    assertThat(routeService.getParamsByPreferences(regionInfo)).doesNotContainKey("conc");
  }

  /* See https://redmine.buzzhives.com/issues/7663. */
  @Test public void excludeWheelchairInfo() {
    when(tripPreferences.isWheelchairPreferred()).thenReturn(false);
    assertThat(routeService.getParamsByPreferences(regionInfo)).doesNotContainKey("wheelchair");
  }

  @Test public void excludeWheelchairWhenRegionDoesNotSupportsIt() {
    // Arrange.
    when(tripPreferences.isWheelchairPreferred()).thenReturn(true);

    // Act.
    final Map<String, Object> map = routeService.getParamsByPreferences(regionInfo);

    // Assert.
    assertThat(map).doesNotContainKey("wheelchair");
  }

  @Test public void shoudNotExcludeWheelchairWhenRegionDoesSupportsIt() {
    // Arrange.
    when(regionInfo.transitWheelchairAccessibility()).thenReturn(true);
    when(tripPreferences.isWheelchairPreferred()).thenReturn(true);

    // Act
    final Map<String, Object> map = routeService.getParamsByPreferences(regionInfo);

    // Assert.
    assertThat(map).containsKey("wheelchair");
  }

  @Test public void shouldIncludeOptionDepartAfter() {
    final Query query = createQuery();
    query.setTimeTag(TimeTag.createForLeaveAfter(25251325));

    final Map<String, Object> options = routeService.toOptions(query, regionInfo);
    assertThat(options)
        .containsEntry("arriveBefore", "0")
        .containsEntry("departAfter", "25251325");
  }

  @Test public void shouldContainOptionIncludeStops() {
    final Query query = createQuery();
    final Map<String, Object> options = routeService.toOptions(query, regionInfo);
    assertThat(options).containsEntry("includeStops", "1");
  }

  @Test public void shouldContainOptionIncludeStopsByDefault() {
    final Query query = createQuery();

    final Map<String, Object> options = routeService.toOptions(query, regionInfo);
    assertThat(options).containsEntry("includeStops", "1");
  }

  @Test public void includeCo2Profile() {
    final Map<String, Float> co2Profile = new HashMap<>();
    co2Profile.put("a", 2f);
    co2Profile.put("b", 5f);
    when(co2Preferences.getCo2Profile()).thenReturn(co2Profile);
    assertThat(routeService.getParamsByPreferences(regionInfo))
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
