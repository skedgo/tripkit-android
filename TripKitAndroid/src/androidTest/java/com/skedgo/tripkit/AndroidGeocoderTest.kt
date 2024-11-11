package com.skedgo.tripkit

import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.geocoding.ReverseGeocodable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidGeocoderTest {
    private var factory: ReverseGeocodable? = null

    @Before
    fun before() {
        factory = AndroidGeocoder(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    /* This test may fail if devices don't have network. */
    @Test
    fun reverseGeocodeInCA() {
        val subscriber = factory!!.getAddress(33.956252, -118.217896).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue("8677 Evergreen Ave, South Gate, CA 90280, USA")
    }
}