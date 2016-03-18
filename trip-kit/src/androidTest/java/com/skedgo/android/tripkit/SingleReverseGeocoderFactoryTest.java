package com.skedgo.android.tripkit;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.observers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class SingleReverseGeocoderFactoryTest {
  private SingleReverseGeocoderFactory factory;

  @Before public void before() {
    factory = new SingleReverseGeocoderFactory(InstrumentationRegistry.getInstrumentation().getTargetContext());
  }

  /* This test may fail if devices don't have network. */
  @Test public void reverseGeocodeInCA() {
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    factory.call(
        ImmutableReverseGeocodingParams.builder()
            .lat(33.956252)
            .lng(-118.217896)
            .maxResults(1)
            .build()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue("8677 Evergreen Ave\nSouth Gate, CA 90280");
  }
}