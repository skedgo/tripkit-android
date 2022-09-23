package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.Region;

import com.skedgo.tripkit.data.regions.RegionService;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observable;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class LocationInfoServiceImplTest {
  @Mock
  LocationInfoApi api;
  @Mock
  RegionService regionService;
  private LocationInfoServiceImpl service;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new LocationInfoServiceImpl(api, regionService);
  }

  @Test public void fetchLocationInfoByRegionUrl() {
    final Region region = mock(Region.class);
    when(region.getURLs(null))
        .thenReturn(new ArrayList<>(singletonList("https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp")));
    when(regionService.getRegionByLocationAsync(any(Location.class)))
        .thenReturn(Observable.just(region));

    final Location location = new Location(1.0, 2.0);
    final LocationInfo locationInfo = mock(LocationInfo.class);
    when(api.fetchLocationInfoAsync(
        eq("https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/locationInfo.json"),
        eq(location.getLat()),
        eq(location.getLon())
    )).thenReturn(Observable.just(locationInfo));

    final TestObserver<LocationInfo> subscriber = service.getLocationInfoAsync(location).test();

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(locationInfo);
  }

  @Test public void fetchLocationInfoByRegionUrls() {
    final Region region = mock(Region.class);
    when(region.getURLs(null)).thenReturn(new ArrayList<>(Arrays.asList(
        "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp",
        "https://inflationary-au-nsw-sydney.tripgo.skedgo.com/satapp",
        "https://hadron-fr-b-bordeaux.tripgo.skedgo.com/satapp"
    )));
    when(regionService.getRegionByLocationAsync(any(Location.class)))
        .thenReturn(Observable.just(region));

    final Location location = new Location(1.0, 2.0);
    final LocationInfo locationInfo = mock(LocationInfo.class);
    when(api.fetchLocationInfoAsync(
        anyString(),
        eq(location.getLat()),
        eq(location.getLon())
    )).thenAnswer(new Answer<Observable<LocationInfo>>() {
      @Override
      public Observable<LocationInfo> answer(InvocationOnMock invocation) throws Throwable {
        final String url = invocation.getArgument(0);
        switch (url) {
          case "https://inflationary-au-nsw-sydney.tripgo.skedgo.com/satapp/locationInfo.json":
            return Observable.just(locationInfo);
          case "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/locationInfo.json":
            return Observable.empty();
          default:
            return Observable.error(new RuntimeException());
        }
      }
    });

    final TestObserver<LocationInfo> subscriber = service.getLocationInfoAsync(location).test();

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(locationInfo);
  }
}