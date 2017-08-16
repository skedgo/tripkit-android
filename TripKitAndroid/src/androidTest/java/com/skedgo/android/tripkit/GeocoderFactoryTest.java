package com.skedgo.android.tripkit;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.observers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class GeocoderFactoryTest {
  private GeocoderFactory factory;

  @Before public void before() {
    factory = new GeocoderFactory(InstrumentationRegistry.getInstrumentation().getTargetContext());
  }

  /* This test may fail if devices don't have network. */
  @Test public void reverseGeocodeInCA() {
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    factory.getFirstAddress(33.956252, -118.217896, 1).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue("8677-8681 Evergreen Avenue South Gate, CA 90280");
  }
}