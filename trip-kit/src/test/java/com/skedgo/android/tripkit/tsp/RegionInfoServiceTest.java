package com.skedgo.android.tripkit.tsp;

import com.skedgo.android.tripkit.BuildConfig;
import com.skedgo.android.tripkit.TestRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import dagger.Lazy;
import rx.Observable;
import rx.exceptions.CompositeException;
import rx.observers.TestSubscriber;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class RegionInfoServiceTest {
  @Rule public final MockitoRule rule = MockitoJUnit.rule();
  @Mock RegionInfoApi api;
  private RegionInfoService service;

  @Before public void before() {
    service = new RegionInfoService(new Lazy<RegionInfoApi>() {
      @Override public RegionInfoApi get() {
        return api;
      }
    });
  }

  /**
   * We manage to fetch via first server, then we ignore second server.
   */
  @Test public void fetchRegionInfoSuccessfully() {
    final RegionInfo regionInfo = ImmutableRegionInfo.builder()
        .transitWheelchairAccessibility(true)
        .build();
    final RegionInfoResponse response = ImmutableRegionInfoResponse.builder()
        .regions(singletonList(regionInfo))
        .build();
    when(api.fetchRegionInfoAsync(
        eq("http://tripgo.com/regionInfo.json"),
        eq(ImmutableRegionInfoBody.of("AU"))
    )).thenReturn(Observable.just(response));

    final List<String> baseUrls = Arrays.asList(
        "http://tripgo.com/",
        "http://riogo.com/"
    );
    final TestSubscriber<RegionInfo> subscriber = new TestSubscriber<>();
    service.fetchRegionInfoAsync(baseUrls, "AU").subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(regionInfo);
  }

  /**
   * When we fail to fetch via first server but manage via second server.
   */
  @Test public void fetchRegionInfoSuccessfullyVia2ndServer() {
    final RegionInfo regionInfo = ImmutableRegionInfo.builder()
        .transitWheelchairAccessibility(true)
        .build();
    final RegionInfoResponse response = ImmutableRegionInfoResponse.builder()
        .regions(singletonList(regionInfo))
        .build();
    when(api.fetchRegionInfoAsync(
        eq("http://tripgo.com/regionInfo.json"),
        eq(ImmutableRegionInfoBody.of("AU"))
    )).thenReturn(Observable.just(response));
    final RuntimeException error = new RuntimeException("1st server is down");
    when(api.fetchRegionInfoAsync(anyString(), any(RegionInfoBody.class)))
        .thenReturn(Observable.<RegionInfoResponse>error(error))
        .thenReturn(Observable.just(response));

    final List<String> baseUrls = Arrays.asList(
        "http://tripgo.com/",
        "http://riogo.com/"
    );
    final TestSubscriber<RegionInfo> subscriber = new TestSubscriber<>();
    service.fetchRegionInfoAsync(baseUrls, "sydney").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(regionInfo);

    verify(api, times(2)).fetchRegionInfoAsync(
        anyString(),
        any(RegionInfoBody.class)
    );
  }

  /**
   * When we fail to fetch via both servers.
   */
  @Test public void failToFetchRegionInfo() {
    final RuntimeException firstError = new RuntimeException("1st server is down");
    final RuntimeException secondError = new RuntimeException("2nd server is down");
    when(api.fetchRegionInfoAsync(anyString(), any(RegionInfoBody.class)))
        .thenReturn(Observable.<RegionInfoResponse>error(firstError))
        .thenReturn(Observable.<RegionInfoResponse>error(secondError));

    final List<String> baseUrls = Arrays.asList(
        "http://tripgo.com/",
        "http://riogo.com/"
    );
    final TestSubscriber<RegionInfo> subscriber = new TestSubscriber<>();
    service.fetchRegionInfoAsync(baseUrls, "sydney").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(CompositeException.class);

    verify(api, times(2)).fetchRegionInfoAsync(
        anyString(),
        any(RegionInfoBody.class)
    );
  }
}