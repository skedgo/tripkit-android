package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.tripkit.tsp.RegionInfoApi;
import com.skedgo.android.tripkit.tsp.RegionInfoBody;
import com.skedgo.android.tripkit.tsp.RegionInfoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.internal.Factory;
import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RegionServiceImplTest {
  @Mock Cache<List<Region>> regionCache;
  @Mock Cache<Map<String, TransportMode>> modeCache;
  @Mock Func1<String, RegionInfoApi> regionInfoApiFactory;
  @Mock RegionsFetcher regionsFetcher;
  @Mock Factory<RegionInfoApi> regionInfoApiProvider;
  @Mock Factory<RegionInfoService> regionInfoServiceProvider;
  private RegionServiceImpl regionService;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    regionService = new RegionServiceImpl(
        regionCache,
        modeCache,
        regionInfoApiFactory,
        regionsFetcher,
        regionInfoApiProvider,
        regionInfoServiceProvider
    );
  }

  @Test public void shouldPropagateNullPointerExceptionIfLocationIsNull() {
    final TestSubscriber<Region> subscriber = new TestSubscriber<>();
    regionService.getRegionByLocationAsync(null).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnErrorEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(NullPointerException.class)
        .extractingResultOf("getMessage")
        .containsExactly("Location is null");
  }

  @Test public void shouldTakeFirstFoundRegion() {
    final Region Sydney = new Region();
    Sydney.setName("AU_NSW_Sydney");
    Sydney.setEncodedPolyline("nwcvE_fno[owyR??mcjRnwyR?");

    final Region NewYork = new Region();
    NewYork.setName("US_NY_NewYorkCity");
    NewYork.setEncodedPolyline("oecvFnzhdM_}tA??o~oE~|tA?");

    when(regionCache.getAsync())
        .thenReturn(Observable.just(Arrays.asList(Sydney, NewYork)));

    final TestSubscriber<Region> subscriber = new TestSubscriber<>();
    regionService.getRegionByLocationAsync(new Location(-33.86749, 151.20699))
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final List<Region> regions = subscriber.getOnNextEvents();
    assertThat(regions).hasSize(1);
    assertThat(regions.get(0)).isSameAs(Sydney);
  }

  @Test public void shouldPropagateOutOfRegionsExceptionIfNoRegionIsFound() {
    final Region Sydney = new Region();
    Sydney.setName("AU_NSW_Sydney");
    Sydney.setEncodedPolyline("nwcvE_fno[owyR??mcjRnwyR?");

    final Region NewYork = new Region();
    NewYork.setName("US_NY_NewYorkCity");
    NewYork.setEncodedPolyline("oecvFnzhdM_}tA??o~oE~|tA?");

    when(regionCache.getAsync())
        .thenReturn(Observable.just(Arrays.asList(Sydney, NewYork)));

    final TestSubscriber<Region> subscriber = new TestSubscriber<>();
    final Location location = new Location(1, 2);
    regionService.getRegionByLocationAsync(location).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    final List<Throwable> errors = subscriber.getOnErrorEvents();
    assertThat(errors)
        .hasSize(1)
        .hasOnlyElementsOfType(OutOfRegionsException.class)
        .extractingResultOf("getMessage")
        .containsExactly("Location lies outside covered area");
    // noinspection ThrowableResultOfMethodCallIgnored
    assertThat(((OutOfRegionsException) errors.get(0)).getLocation()).isSameAs(location);
  }

  @Test public void shouldTakeAllCitiesInRegions() {
    final Region AU = new Region();
    final Region.City Sydney = new Region.City();
    Sydney.setName("Sydney");
    final Region.City Newcastle = new Region.City();
    Newcastle.setName("Newcastle");
    AU.setCities(new ArrayList<>(Arrays.asList(Sydney, Newcastle)));

    final Region US = new Region();
    final Region.City NewYork = new Region.City();
    NewYork.setName("New York");
    final Region.City SanJose = new Region.City();
    SanJose.setName("San Jose");
    US.setCities(new ArrayList<>(Arrays.asList(NewYork, SanJose)));

    when(regionCache.getAsync())
        .thenReturn(Observable.just(Arrays.asList(AU, US)));

    final TestSubscriber<Location> subscriber = new TestSubscriber<>();
    regionService.getCitiesAsync().subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final List<Location> cities = subscriber.getOnNextEvents();
    assertThat(cities).containsExactly(Sydney, Newcastle, NewYork, SanJose);
  }

  @Test public void shouldTakeTransportModesFromModesLoader() {
    final Map<String, TransportMode> modeMap = new HashMap<>();
    modeMap.put("car", new TransportMode());
    modeMap.put("walk", new TransportMode());
    when(modeCache.getAsync()).thenReturn(Observable.just(modeMap));

    final TestSubscriber<Map<String, TransportMode>> subscriber = new TestSubscriber<>();
    regionService.getTransportModesAsync().subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final Map<String, TransportMode> actualModeMap = subscriber.getOnNextEvents().get(0);
    assertThat(actualModeMap).isEqualTo(modeMap);
  }

  @Test public void shouldTakeRegionsFromRegionsLoader() {
    final List<Region> regions = Arrays.asList(new Region(), new Region());
    when(regionCache.getAsync()).thenReturn(Observable.just(regions));

    final TestSubscriber<List<Region>> subscriber = new TestSubscriber<>();
    regionService.getRegionsAsync().subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    assertThat(subscriber.getOnNextEvents().get(0)).isEqualTo(regions);
  }

  @Test public void shouldFetchParatransit() {
    final RegionInfoApi regionInfoApi = mock(RegionInfoApi.class);
    final Paratransit paratransit = new Paratransit(
        "http://accessla.org/",
        "Access",
        "1.800.883.1295"
    );
    final RegionInfo regionInfo = ImmutableRegionInfo.builder()
        .paratransit(paratransit)
        .build();
    final RegionInfoResponse response = ImmutableRegionInfoResponse.builder()
        .regions(singletonList(regionInfo))
        .build();
    when(regionInfoApi.fetchRegionInfo(any(RegionInfoBody.class)))
        .thenReturn(response);
    when(regionInfoApiFactory.call(eq("https://lepton-us-ca-losangeles.tripgo.skedgo.com/satapp")))
        .thenReturn(regionInfoApi);

    final Region region = new Region();
    region.setURLs(new ArrayList<>(singletonList("https://lepton-us-ca-losangeles.tripgo.skedgo.com/satapp")));
    region.setName("US_CA_LosAngeles");

    final TestSubscriber<Paratransit> subscriber = new TestSubscriber<>();
    regionService.fetchParatransitByRegionAsync(region)
        .subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();

    assertThat(subscriber.getOnNextEvents())
        .containsExactly(paratransit);
  }

  @Test public void shouldInvalidateCachesAfterRefreshing() {
    when(regionsFetcher.fetchAsync()).thenReturn(Observable.<Void>empty());
    final TestSubscriber<Void> subscriber = new TestSubscriber<>();
    regionService.refreshAsync().subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();

    verify(modeCache, times(1)).invalidate();
    verify(regionCache, times(1)).invalidate();
  }
}