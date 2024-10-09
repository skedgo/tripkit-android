package com.skedgo.tripkit.booking

import android.os.Parcel
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.booking.BookingAction.CREATOR.createFromParcel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BookingActionTest {

    @Test
    fun parse() {
        val gson = GsonBuilder().serializeNulls().create()

        var testJson = """
            {
              "title": "Next",
              "url": "http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book"
            }
        """.trimIndent()
        var actual = gson.fromJson(testJson, BookingAction::class.java)
        assertEquals("Next", actual.title)
        assertEquals(true, actual.enable)
        assertEquals(false, actual.done)
        assertEquals("http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book", actual.url)

        testJson = """
            {
              "title": "Next",
              "enabled": false,
              "url": "http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book"
            }
        """.trimIndent()
        actual = gson.fromJson(testJson, BookingAction::class.java)
        assertEquals("Next", actual.title)
        assertEquals(false, actual.enable)
        assertEquals(false, actual.done)
        assertEquals("http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book", actual.url)

        testJson = """
            {
              "title": "Next",
              "done": true,
              "url": "http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book"
            }
        """.trimIndent()
        actual = gson.fromJson(testJson, BookingAction::class.java)
        assertEquals("Next", actual.title)
        assertEquals(true, actual.enable)
        assertEquals(true, actual.done)
        assertEquals("http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book", actual.url)
    }

    @Test
    fun parcelable() {
        val expected = BookingAction().apply {
            url = "url"
            enable = false
            title = "title"
        }

        // Mock the Parcel
        val parcel = mockk<Parcel>(relaxed = true)

        // Mock writing to Parcel, accounting for all string fields and boolean fields
        every { parcel.writeString(any()) } just Runs
        every { parcel.writeString(expected.title) } just Runs
        every { parcel.writeString(expected.url) } just Runs
        every { parcel.writeString(expected.enable.toString()) } just Runs

        // Mock resetting the data position
        every { parcel.setDataPosition(0) } just Runs

        // Write to parcel
        expected.writeToParcel(parcel, 0)

        // Mock reading from Parcel, including strings and booleans
        every { parcel.readString() } returnsMany listOf(expected.title, expected.enable.toString(), expected.url)

        // Create actual object from parcel
        val actual: BookingAction = BookingAction.CREATOR.createFromParcel(parcel)

        // Perform assertions
        assertEquals("URL should be parcelled properly", expected.url, actual.url)
        assertEquals("Title should be parcelled properly", expected.title, actual.title)
        assertEquals("Enable flag should be parcelled properly", expected.enable, actual.enable)
    }
}