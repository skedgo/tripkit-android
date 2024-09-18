package com.skedgo.tripkit;

import com.skedgo.tripkit.geocoding.ReverseGeocodable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.reactivex.observers.TestObserver;

@RunWith(AndroidJUnit4.class)
public class AndroidGeocoderTest {
    private ReverseGeocodable factory;

    @Before
    public void before() {
        factory = new AndroidGeocoder(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    /* This test may fail if devices don't have network. */
    @Test
    public void reverseGeocodeInCA() {
        final TestObserver<String> subscriber = factory.getAddress(33.956252, -118.217896).test();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertValue("8677 Evergreen Ave, South Gate, CA 90280, USA");
    }
}