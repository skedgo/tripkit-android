package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import com.skedgo.android.common.model.RegionsResponse;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.common.util.Gsons;
import com.squareup.okhttp.OkHttpClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.Arrays;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static android.util.DisplayMetrics.DENSITY_HIGH;
import static android.util.DisplayMetrics.DENSITY_MEDIUM;
import static android.util.DisplayMetrics.DENSITY_XHIGH;
import static android.util.DisplayMetrics.DENSITY_XXHIGH;
import static android.util.DisplayMetrics.DENSITY_XXXHIGH;
import static com.skedgo.android.common.util.TransportModeUtils.getIconUrlForTransportMode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RegionsApiTest {
  @Rule public final ErrorCollector errorCollector = new ErrorCollector();
  private RegionsApi regionsApi;

  @Before
  public void setUp() {
    regionsApi = new RestAdapter.Builder()
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setEndpoint("https://tripgo.skedgo.com/satapp")
        .setConverter(new GsonConverter(Gsons.createForRegion()))
        .setClient(new OkClient(new OkHttpClient()))
        .build()
        .create(RegionsApi.class);
  }

  @Test
  public void modeIconsShouldBeReadyForXum() {
    assertModeIcons(new RegionsApi.RequestBodyContent(2, "xum"));
  }

  @Test
  public void modeIconsShouldBeReadyForOptus() {
    assertModeIcons(new RegionsApi.RequestBodyContent(2, "optus"));
  }

  @Test
  public void modeIconsShouldBeReadyForTripGoStable() {
    assertModeIcons(new RegionsApi.RequestBodyContent(2, ""));
  }

  @Test
  public void modeIconsShouldBeReadyForTripGoBeta() {
    assertModeIcons(new RegionsApi.RequestBodyContent(2, "beta"));
  }

  @Test
  public void regionsForXumShouldBeValid() {
    assertRegionsResponse(new RegionsApi.RequestBodyContent(2, "xum"));
  }

  @Test
  public void regionsForOptusShouldBeValid() {
    assertRegionsResponse(new RegionsApi.RequestBodyContent(2, "optus"));
  }

  @Test
  public void regionsForTripGoStableShouldBeValid() {
    assertRegionsResponse(new RegionsApi.RequestBodyContent(2, ""));
  }

  @Test
  public void regionsForTripGoBetaShouldBeValid() {
    assertRegionsResponse(new RegionsApi.RequestBodyContent(2, "beta"));
  }

  private void assertRegionsResponse(RegionsApi.RequestBodyContent requestBodyContent) {
    final TestSubscriber<RegionsResponse> subscriber = new TestSubscriber<>();
    regionsApi.fetchRegionsAsync(requestBodyContent).subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final RegionsResponse response = subscriber.getOnNextEvents().get(0);
    RegionAssertionUtils.assertRegions(response.getRegions());
    RegionAssertionUtils.assertTransportModes(response.getTransportModes());
  }

  private void assertModeIcons(RegionsApi.RequestBodyContent requestBodyContent) {
    final TestSubscriber<Throwable> s = new TestSubscriber<>();
    regionsApi.fetchRegionsAsync(requestBodyContent)
        .flatMap(new Func1<RegionsResponse, Observable<TransportMode>>() {
          @Override
          public Observable<TransportMode> call(RegionsResponse response) {
            return Observable.from(response.getTransportModes());
          }
        })
        .flatMap(new Func1<TransportMode, Observable<String>>() {
          @Override
          public Observable<String> call(final TransportMode mode) {
            return Observable
                .from(Arrays.asList(
                    DENSITY_MEDIUM,
                    DENSITY_HIGH,
                    DENSITY_XHIGH,
                    DENSITY_XXHIGH,
                    DENSITY_XXXHIGH
                ))
                .map(new Func1<Integer, String>() {
                  @Override
                  public String call(Integer dpi) {
                    final Resources resources = mockResourcesForDensity(dpi);
                    return getIconUrlForTransportMode(resources, mode);
                  }
                });
          }
        })
        .filter(new Func1<String, Boolean>() {
          @Override
          public Boolean call(String url) {
            return url != null;
          }
        })
        .flatMap(new Func1<String, Observable<Throwable>>() {
          @Override
          public Observable<Throwable> call(final String url) {
            return Observable
                .create(new Observable.OnSubscribe<Throwable>() {
                  @Override
                  public void call(Subscriber<? super Throwable> subscriber) {
                    try {
                      final Bitmap icon = BitmapFactory.decodeStream(new URL(url).openStream());
                      icon.recycle();
                    } catch (Throwable e) {
                      subscriber.onNext(e);
                    }
                    subscriber.onCompleted();
                  }
                })
                .subscribeOn(Schedulers.io());
          }
        })
        .doOnNext(new Action1<Throwable>() {
          @Override
          public void call(Throwable error) {
            errorCollector.addError(error);
          }
        })
        .subscribe(s);
    s.awaitTerminalEvent();
    s.assertTerminalEvent();
    s.assertNoErrors();
  }

  @NonNull
  private Resources mockResourcesForDensity(int densityDpi) {
    final DisplayMetrics displayMetrics = new DisplayMetrics();
    displayMetrics.densityDpi = densityDpi;

    final Resources resources = mock(Resources.class);
    when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
    return resources;
  }
}