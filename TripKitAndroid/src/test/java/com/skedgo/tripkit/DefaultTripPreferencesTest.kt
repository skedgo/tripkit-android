package com.skedgo.tripkit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Java6Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultTripPreferencesTest {
    private var preferences: DefaultTripPreferences? = null

    @Before
    fun before() {
        preferences = DefaultTripPreferences(
            ApplicationProvider.getApplicationContext<Context>().getSharedPreferences(
                "SomePreferences",
                Context.MODE_PRIVATE
            )
        )
    }

    @Test
    fun storeAndQueryConcessionPricingPreference() {
        Java6Assertions.assertThat(preferences!!.isConcessionPricingPreferred()).isFalse()
        preferences!!.setConcessionPricingPreferred(true)
        Java6Assertions.assertThat(preferences!!.isConcessionPricingPreferred()).isTrue()
    }

    @Test
    fun storeAndQueryWheelchairPreference() {
        Java6Assertions.assertThat(preferences!!.isWheelchairPreferred()).isFalse()
        preferences!!.setWheelchairPreferred(true)
        Java6Assertions.assertThat(preferences!!.isWheelchairPreferred()).isTrue()
    }
}