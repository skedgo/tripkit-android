package com.skedgo.tripkit.booking

import android.os.Parcel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StepperFormFieldTest {

    @Test
    fun testParcelable() {
        val expected = StepperFormField().apply {
            setValue(5)
            minValue = 0
            maxValue = 10
        }

        val parcel = mockk<Parcel>(relaxed = true) // Mock Parcel object
        every { parcel.writeInt(any()) } returns Unit // Mocking the parcel behavior

        expected.writeToParcel(parcel, 0)

        verify (exactly = 7) { parcel.writeInt(any()) } // Verifying that writeInt was called 3 times

        every { parcel.readInt() } returnsMany listOf(
            1,
            1,
            1,
            1,
            5,
            0,
            10
        ) // Mocking the parcel reading

        val actual = StepperFormField.CREATOR.createFromParcel(parcel)

        assertEquals(expected.getValue(), actual.getValue())
        assertEquals(expected.minValue, actual.minValue)
        assertEquals(expected.maxValue, actual.maxValue)
    }
}