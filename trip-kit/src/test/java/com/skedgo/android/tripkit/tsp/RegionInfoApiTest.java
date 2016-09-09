package com.skedgo.android.tripkit.tsp;

import com.skedgo.android.tripkit.BuildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static rx.schedulers.Schedulers.immediate;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RegionInfoApiTest {
  @Rule public final MockWebServerRule serverRule = new MockWebServerRule();
  private RegionInfoApi api;

  @Before public void before() {
    api = new Retrofit.Builder()
        .baseUrl(serverRule.server.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(immediate()))
        .build()
        .create(RegionInfoApi.class);
  }
}