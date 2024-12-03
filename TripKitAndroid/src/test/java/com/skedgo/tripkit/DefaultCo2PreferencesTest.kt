package com.skedgo.tripkit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Java6Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultCo2PreferencesTest {
    private var preferences: DefaultCo2Preferences? = null

    @Before
    fun before() {
        preferences = DefaultCo2Preferences(
            ApplicationProvider.getApplicationContext<Context>().getSharedPreferences(
                "SomePreferences",
                Context.MODE_PRIVATE
            )
        )
    }

    @Test
    fun storeAndQueryCo2Profile() {
        preferences!!.setEmissions("a", 3f)
        preferences!!.setEmissions("b", 5f)
        preferences!!.setEmissions("c", 7f)
        Java6Assertions.assertThat(preferences!!.getCo2Profile())
            .hasSize(3)
            .containsEntry("a", 3f)
            .containsEntry("b", 5f)
            .containsEntry("c", 7f)
    }
}