package com.skedgo.android.tripkit.tsp;

import com.skedgo.android.tripkit.BuildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import dagger.Lazy;
import rx.Observable;
import rx.observers.TestSubscriber;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RegionInfoServiceTest {
  @Rule public final MockitoRule rule = MockitoJUnit.rule();
  @Mock RegionInfoApi regionInfoApi;
  private RegionInfoService service;

  @Before public void before() {
    service = new RegionInfoService(new Lazy<RegionInfoApi>() {
      @Override public RegionInfoApi get() {
        return regionInfoApi;
      }
    });
  }

  @Test public void fetchRegionInfoSuccessfully() {
    final RegionInfo regionInfo = ImmutableRegionInfo.builder()
        .transitWheelchairAccessibility(true)
        .build();
    final RegionInfoResponse response = ImmutableRegionInfoResponse.builder()
        .regions(singletonList(regionInfo))
        .build();
    when(regionInfoApi.fetchRegionInfoAsync(
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
}