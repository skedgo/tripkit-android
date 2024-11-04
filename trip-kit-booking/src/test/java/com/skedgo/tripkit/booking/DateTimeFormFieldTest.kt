package com.skedgo.tripkit.booking

import android.os.Parcel
import com.skedgo.tripkit.booking.DateTimeFormField.CREATOR.createFromParcel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DateTimeFormFieldTest {
    @Test
    fun parcelableTest() {
        // Step 1: Create the expected DateTimeFormField
        val expected = DateTimeFormField().apply {
            setValue(100L) // Set the value
        }

        // Step 2: Mock Parcel and simulate parceling
        val parcel = mockk<Parcel>(relaxed = true)

        // Step 3: Write to the parcel
        expected.writeToParcel(parcel, 0)

        // Simulate parceling process, setDataPosition(0) sets position for reading the parcel
        verify { parcel.writeLong(100L) }

        // Step 4: Simulate reading from parcel
        every { parcel.readLong() } returns 100L

        // Recreate DateTimeFormField from the parcel
        val actual = DateTimeFormField.CREATOR.createFromParcel(parcel)

        // Step 5: Assertions
        assertEquals(expected.getValue(), actual.getValue())
    }
}