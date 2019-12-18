package com.skedgo.tripkit;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.skedgo.tripkit.geocoding.ReverseGeocodable;

@RunWith(AndroidJUnit4.class)
public class AndroidGeocoderTest {
  private ReverseGeocodable factory;

  @Before public void before() {
    factory = new AndroidGeocoder(InstrumentationRegistry.getInstrumentation().getTargetContext());
  }

  /* This test may fail if devices don't have network. */
  @Test public void reverseGeocodeInCA() {
    final TestObserver<String> subscriber = factory.getAddress(33.956252, -118.217896).test();
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue("8677-8681 Evergreen Avenue South Gate, CA 90280");
  }
}