package com.skedgo.tripkit.booking.viewmodel

import android.os.Parcel
import com.skedgo.tripkit.booking.BookingAction
import com.skedgo.tripkit.booking.InputForm
import com.skedgo.tripkit.booking.LinkFormField
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ParamTest {

    @Test
    fun createParamWithUrl() {
        val param = Param.create("url")

        assertEquals(
            "Should use method GET for just a URL",
            LinkFormField.METHOD_GET,
            param.method
        )
    }

    @Test
    fun createParamWithLinkFormField() {
        val linkFormField = LinkFormField().apply {
            method = LinkFormField.METHOD_POST
        }
        val param = Param.create(linkFormField)

        assertEquals(
            "Should parse method from LinkFormField",
            LinkFormField.METHOD_POST,
            param.method
        )
    }

    @Test
    fun createParamWithBookingAction() {
        val bookingAction = BookingAction()
        val postBody = InputForm()
        val param = Param.create(bookingAction, postBody)

        assertEquals(
            "Should use method POST for BookingAction",
            LinkFormField.METHOD_POST,
            param.method
        )
    }

    @Test
    fun parseParamFromParcel() {
        val bookingAction = BookingAction()
        val postBody = InputForm()
        val expected = Param.create(bookingAction, postBody)

        // Mock the Parcel
        val parcel = mockk<Parcel>(relaxed = true)

        // Mock writing to Parcel, including the 'method' field
        every { parcel.writeString(any()) } just Runs
        every { parcel.writeParcelable(any(), any()) } just Runs
        every { parcel.writeSerializable(any()) } just Runs

        // Write to parcel
        expected.writeToParcel(parcel, 0)

        // Mock reading from Parcel, including the 'method' field
        every { parcel.readString() } returnsMany listOf(expected.url, expected.method, expected.hudText)
        every { parcel.readParcelable<InputForm>(any<ClassLoader>()) } returns expected.postBody

        // Mock resetting the parcel data position
        every { parcel.setDataPosition(0) } just Runs

        // Create actual from parcel
        val actual = Param.CREATOR.createFromParcel(parcel)

        // Perform assertions
        assertEquals("URL should be parcelled properly", expected.url, actual.url)
        assertEquals("Method should be parcelled properly", expected.method, actual.method)
        assertEquals("Post body should be parcelled properly", expected.postBody, actual.postBody)
    }
}