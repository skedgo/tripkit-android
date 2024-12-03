package com.skedgo.tripkit

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import io.mockk.MockKAnnotations
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UuidProviderTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var provider: UuidProvider
    private lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initRx()
        preferences = ApplicationProvider.getApplicationContext<Context>().getSharedPreferences(
            "TripKit",
            Context.MODE_PRIVATE
        )
        provider = UuidProvider(preferences)
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    @Test
    fun `generate uuid`() {
        val uuid = provider.call()
        assertNotNull(uuid)
        assertTrue(uuid.isNotEmpty())

        provider = UuidProvider(preferences)
        assertEquals(uuid, provider.call())
    }

    @Test
    fun `use persistent uuid`() {
        preferences.edit()
            .putString("UUID", "Some UUID")
            .apply()
        assertEquals("Some UUID", provider.call())
    }
}