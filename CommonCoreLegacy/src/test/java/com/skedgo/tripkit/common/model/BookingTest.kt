package com.skedgo.tripkit.common.model

import com.google.gson.GsonBuilder
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookingTest {

    companion object {
        private val KEY_TITLE = "title"
        private val KEY_EXTERNAL_ACTIONS = "externalActions"
        private val KEY_QUICK_BOOKINGS_URL = "quickBookingsUrl"
        private val KEY_URL = "url"
    }

    @Test
    fun `should get correct data when parsing with GsonAdaptersBooking via gson`() {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(GsonAdaptersBooking())
            .create()

        val title = "Get a ride"
        val actions = listOf("book")
        val bookingUrl =
            "https://granduni.buzzhives.com/satapp-beta/booking/v1/8ce3829b-9f66-4ae1-87df-259202b06a27/quick"
        val url =
            "https://granduni.buzzhives.com/satapp-beta/booking/8ce3829b-9f66-4ae1-87df-259202b06a27/info"

        val expected: Booking = ImmutableBooking.builder()
            .quickBookingsUrl(bookingUrl)
            .title(title)
            .url(url)
            .externalActions(actions)
            .build()

        val actual = gson.toJsonTree(expected).asJsonObject
        assertEquals(actual.has(KEY_TITLE), true)
        assertEquals(actual.get(KEY_TITLE).asString, title)

        assertEquals(actual.has(KEY_EXTERNAL_ACTIONS), true)
        val actualActionsArray = actual.get(KEY_EXTERNAL_ACTIONS).asJsonArray
        val actualActionsList = mutableListOf<String>()
        for (i in 0 until actualActionsArray.size()) {
            actualActionsList.add(actualActionsArray.get(i).asString)
        }
        assertEquals(actualActionsList, actions)

        assertEquals(actual.has(KEY_QUICK_BOOKINGS_URL), true)
        assertEquals(actual.get(KEY_QUICK_BOOKINGS_URL).asString, bookingUrl)

        assertEquals(actual.has(KEY_URL), true)
        assertEquals(actual.get(KEY_URL).asString, url)
    }
}