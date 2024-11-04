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
class SwitchFormFieldTest {

    @Test
    fun parcelableTest() {
        val expected = SwitchFormField().apply {
            setValue(true)
            keyboardType = "TEXT"
        }

        val parcel = mockk<Parcel>(relaxed = true)

        // Writing to parcel
        expected.writeToParcel(parcel, 0)
        verify { parcel.writeInt(FormField.SWITCH) } // true is written as 1 in parcel
        verify { parcel.writeString("TEXT") }

        // Simulating reading from parcel
        every { parcel.readInt() } returns 1 // true value
        every { parcel.readString() } returnsMany listOf(
            null, null, null, null, null,
            "TEXT", "true"
        )

        val actual = SwitchFormField.CREATOR.createFromParcel(parcel)

        // Assertions
        assertEquals(expected.getValue(), actual.getValue())
        assertEquals(expected.keyboardType, actual.keyboardType)
    }
}
