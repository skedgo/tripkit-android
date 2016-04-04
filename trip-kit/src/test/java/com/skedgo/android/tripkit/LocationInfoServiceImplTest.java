package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LocationInfoServiceImplTest {

  @Mock LocationInfoApi api;
  @Mock RegionService regionService;
  private LocationInfoServiceImpl locationInfoService;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    locationInfoService = new LocationInfoServiceImpl(api, regionService);
  }

  @Test public void shouldFetchLocationInfoFromFirstRegionUrl() {

    final TestSubscriber<LocationInfo> subscriber = new TestSubscriber<>();

    Location location = new Location();
    double lat = 1.0;
    double lng = 2.0;

    location.setLat(lat);
    location.setLon(lng);

    final Region london = mock(Region.class);
    when(london.getURLs()).thenReturn(new ArrayList<>(Arrays.asList("")));

    Region region = new Region();
    ArrayList<String> urls = new ArrayList<>(1);
    urls.add("");
    region.setURLs(urls);

    final LocationInfo locInfo = mock(LocationInfo.class);

    when(regionService.getRegionByLocationAsync(any(Location.class)))
        .thenReturn(Observable.just(region));

    when(api.getLocationInfoResponseAsync(anyString(), eq(lat), eq(lng)))
        .thenReturn(Observable.just(locInfo));

    locationInfoService.getLocationInfoResponseAsync(location).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final List<LocationInfo> infos = subscriber.getOnNextEvents();
    assertThat(infos).hasSize(1);
    assertThat(infos.get(0)).isEqualTo(locInfo);
  }
}
