package com.skedgo.tripkit.booking

import android.os.Parcel
import com.skedgo.tripkit.booking.OptionFormField.CREATOR.createFromParcel
import com.skedgo.tripkit.booking.OptionFormField.OptionValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.ArrayList

@RunWith(RobolectricTestRunner::class)
class OptionFormFieldTest {

    @Test
    fun parcelableTest() {
        val optionValue1 = OptionFormField.OptionValue("A", "a")
        val optionValue2 = OptionFormField.OptionValue("B", "b")
        val list = mutableListOf(optionValue1, optionValue2)

        val expected = OptionFormField().apply {
            setValue(optionValue1)
            allValues = list
        }

        val parcel = mockk<Parcel>(relaxed = true)

        // Writing to parcel
        expected.writeToParcel(parcel, 0)

        verify { parcel.writeParcelable(optionValue1, 0) }
        verify { parcel.writeTypedList(list) }

        // Simulating reading from parcel
        every { parcel.readParcelable<OptionFormField.OptionValue>(any()) } returns optionValue1
        every { parcel.createTypedArrayList(OptionFormField.OptionValue.CREATOR) } returns ArrayList(list)

        val actual = OptionFormField.CREATOR.createFromParcel(parcel)

        // Assertions
        assertEquals("A", actual.getValue()?.title)
        assertEquals("a", actual.getValue()?.value)

        val all = actual.allValues
        assertEquals(2, all.size)
        assertEquals("A", all[0].title)
        assertEquals("a", all[0].value)
        assertEquals("B", all[1].title)
        assertEquals("b", all[1].value)
    }
}