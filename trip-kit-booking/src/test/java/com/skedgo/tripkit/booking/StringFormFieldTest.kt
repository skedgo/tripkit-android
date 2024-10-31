package com.skedgo.tripkit.booking

import android.os.Parcel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.booking.StringFormField
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StringFormFieldTest {

    @Test
    fun parseTest() {
        val gson: Gson = GsonBuilder().serializeNulls().create()

        // Test 1
        var testParse = """
            {
              "type": "string",
              "title": "Interior",
              "id": "interior",
              "readOnly": true,
              "value": "GOOD",
              "keyboardType": "TEXT"
            }
        """.trimIndent()

        var actual: StringFormField = gson.fromJson(testParse, StringFormField::class.java)
        assertEquals("Interior", actual.title)
        assertEquals("interior", actual.id)
        assertTrue(actual.readOnly)
        assertEquals("string", actual.type)
        assertEquals("GOOD", actual.getValue())
        assertEquals("TEXT", actual.keyboardType)
        assertFalse(actual.hidden)

        // Test 2
        testParse = """
            {
              "type": "string",
              "title": "Car",
              "id": "car",
              "hidden": true,
              "value": "386XFI",
              "keyboardType": "TEXT"
            }
        """.trimIndent()

        actual = gson.fromJson(testParse, StringFormField::class.java)
        assertEquals("Car", actual.title)
        assertEquals("car", actual.id)
        assertEquals("string", actual.type)
        assertFalse(actual.readOnly)
        assertEquals("386XFI", actual.getValue())
        assertEquals("TEXT", actual.keyboardType)
        assertTrue(actual.hidden)
    }

    @Test
    fun booleanParcelableTest() {
        val expected = StringFormField().apply {
            title = "a"
            id = "string"
            hidden = false
            readOnly = false
            keyboardType = "TEXT"
        }

        val parcel = mockk<Parcel>(relaxed = true)
        expected.writeToParcel(parcel, 0)

        verify { parcel.writeString("string") }
        verify { parcel.writeString("a") }
        verify { parcel.writeInt(0) }
        verify { parcel.writeInt(0) }
        verify { parcel.writeString("TEXT") }

        // Simulate reading from the parcel
        every { parcel.readString() } returnsMany listOf(
            "string",
            "a",
            null,
            null,
            null,
            "TEXT",
            null
        )
        every { parcel.readInt() } returnsMany listOf(0, 0) // hidden, readOnly

        val actual = StringFormField.CREATOR.createFromParcel(parcel)
        assertEquals(expected.id, actual.id)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.hidden, actual.hidden)
        assertEquals(expected.readOnly, actual.readOnly)
        assertEquals(expected.getValue(), actual.getValue())
        assertEquals(expected.keyboardType, actual.keyboardType)
    }

    @Test
    fun stringParcelableTest() {
        val expected = StringFormField().apply {
            setValue("x")
            id = "string"
            hidden = false
            readOnly = false
            keyboardType = "TEXT"
        }

        val parcel = mockk<Parcel>(relaxed = true)
        expected.writeToParcel(parcel, 0)

        verify { parcel.writeString("string") }
        verify { parcel.writeInt(0) }
        verify { parcel.writeInt(0) }
        verify { parcel.writeString("TEXT") }

        every { parcel.readString() } returnsMany listOf(
            "string",
            null,
            null,
            null,
            null,
            "TEXT",
            "x"
        )
        every { parcel.readInt() } returnsMany listOf(0, 0) // hidden, readOnly

        val actual = StringFormField.CREATOR.createFromParcel(parcel)
        assertEquals(expected.id, actual.id)
        assertNull(actual.title)
        assertEquals(expected.hidden, actual.hidden)
        assertEquals(expected.readOnly, actual.readOnly)
        assertEquals(expected.getValue(), actual.getValue())
        assertEquals(expected.keyboardType, actual.keyboardType)
    }

    @Test
    fun nullTest() {
        val expected = StringFormField().apply {
            id = "string"
            hidden = false
            readOnly = false
            keyboardType = "TEXT"
        }

        val parcel = mockk<Parcel>(relaxed = true)
        expected.writeToParcel(parcel, 0)

        verify { parcel.writeString("string") }
        verify { parcel.writeInt(0) }
        verify { parcel.writeInt(0) }
        verify { parcel.writeString("TEXT") }

        every { parcel.readString() } returnsMany listOf(
            "string",
            null,
            null,
            null,
            null,
            "TEXT",
            null
        )
        every { parcel.readInt() } returnsMany listOf(0, 0) // hidden, readOnly

        val actual = StringFormField.CREATOR.createFromParcel(parcel)
        assertEquals(expected.id, actual.id)
        assertNull(actual.title)
        assertEquals(expected.hidden, actual.hidden)
        assertEquals(expected.readOnly, actual.readOnly)
        assertNull(actual.getValue())
        assertEquals(expected.keyboardType, actual.keyboardType)
    }
}