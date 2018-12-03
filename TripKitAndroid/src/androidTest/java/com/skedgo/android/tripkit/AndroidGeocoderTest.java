package com.skedgo.android.tripkit;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.observers.TestSubscriber;
import skedgo.tripkit.geocoding.ReverseGeocodable;

@RunWith(AndroidJUnit4.class)
public class AndroidGeocoderTest {
  private ReverseGeocodable factory;

  @Before public void before() {
    factory = new AndroidGeocoder(InstrumentationRegistry.getInstrumentation().getTargetContext());
  }

  /* This test may fail if devices don't have network. */
  @Test public void reverseGeocodeInCA() {
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    factory.getAddress(33.956252, -118.217896).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue("8677-8681 Evergreen Avenue South Gate, CA 90280");
  }
}