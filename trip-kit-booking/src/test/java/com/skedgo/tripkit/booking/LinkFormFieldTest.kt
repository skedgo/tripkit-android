package com.skedgo.tripkit.booking

import android.os.Parcel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LinkFormFieldTest {

    @Test
    fun parcelableTest() {
        // Step 1: Create the expected LinkFormField
        val expected = LinkFormField().apply {
            setValue("url")  // Set the value to "url"
        }

        // Step 2: Mock Parcel and simulate parceling
        val parcel = mockk<Parcel>(relaxed = true)

        // Step 3: Write to the parcel
        expected.writeToParcel(parcel, 0)

        // Simulate parceling process, verify that the correct value is written
        verify { parcel.writeString("url") }

        // Step 4: Simulate reading from parcel
        every { parcel.readString() } returns "url"

        // Recreate LinkFormField from the parcel
        val actual = LinkFormField.CREATOR.createFromParcel(parcel)

        // Step 5: Assertions
        assertEquals(expected.getValue(), actual.getValue())
    }
}