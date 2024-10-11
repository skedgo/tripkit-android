package com.skedgo.tripkit.booking

import android.os.Parcel
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.booking.AddressFormField
import com.skedgo.tripkit.booking.AddressFormField.Address
import com.skedgo.tripkit.booking.AddressFormField.CREATOR.createFromParcel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AddressFormFieldTest {

    @Test
    fun parse() {
        val test = """
            {
              "type": "address",
              "title": "Location",
              "id": "location",
              "readOnly": true,
              "value": {
                "lat": 39.76256,
                "lng": -105.03584,
                "address": "Meade St 3225, 80211 Denver",
                "name": "Meade St 3225, 80211 Denver"
              }
            }
        """.trimIndent()

        val gson = GsonBuilder().serializeNulls().create()
        val actual = gson.fromJson(test, AddressFormField::class.java)

        assertEquals("address", actual.type)
        assertEquals("location", actual.id)
        assertEquals("Location", actual.title)
        assertEquals(true, actual.readOnly)

        val address = actual.getValue()
        assertEquals("Meade St 3225, 80211 Denver", address?.address)
        assertEquals("Meade St 3225, 80211 Denver", address?.name)
        assertEquals(39.76256, address?.latitude)
        assertEquals(-105.03584, address?.longitude)
    }

    @Test
    fun parcelable() {
        val expected = AddressFormField()
        val address = AddressFormField.Address().apply {
            latitude = 10.0
            longitude = 12.0
        }
        expected.setValue(address)

        // Mock the Parcel
        val parcel = mockk<Parcel>(relaxed = true)

        // Mock writing to Parcel
        every { parcel.writeInt(any()) } just Runs
        every { parcel.writeParcelable(any(), any()) } just Runs
        every { parcel.setDataPosition(0) } just Runs

        // Write to parcel
        expected.writeToParcel(parcel, 0)

        // Verify writeParcelable and writeInt calls
        verify {
            parcel.writeInt(any())
            parcel.writeParcelable(expected.getValue(), 0)
        }

        // Mock reading from Parcel
        every { parcel.readInt() } returns 5  // For the ADDRESS constant or equivalent int
        every { parcel.readParcelable<AddressFormField.Address>(any<ClassLoader>()) } returns expected.getValue()

        // Reset the data position to simulate reading from the start
        every { parcel.setDataPosition(0) } just Runs

        // Create the actual object from parcel
        val actual: AddressFormField = AddressFormField.CREATOR.createFromParcel(parcel)
        val actualAddress: AddressFormField.Address? = actual.getValue()

        // Perform assertions
        assertEquals("Latitude should be parcelled properly", 10.0, actualAddress?.latitude)
        assertEquals("Longitude should be parcelled properly", 12.0, actualAddress?.longitude)
    }
}