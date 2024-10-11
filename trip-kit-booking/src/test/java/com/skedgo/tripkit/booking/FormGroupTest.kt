package com.skedgo.tripkit.booking

import android.os.Parcel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.skedgo.tripkit.booking.FormField
import com.skedgo.tripkit.booking.FormGroup
import com.skedgo.tripkit.booking.FormGroup.CREATOR.createFromParcel
import com.skedgo.tripkit.booking.LinkFormField
import com.skedgo.tripkit.booking.OptionFormField
import com.skedgo.tripkit.booking.StepperFormField
import com.skedgo.tripkit.booking.StringFormField
import com.skedgo.tripkit.booking.SwitchFormField
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.StringReader
import java.util.ArrayList

@RunWith(RobolectricTestRunner::class)
class FormGroupTest {

    @Test
    fun parseTest() {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(FormField::class.java, FormFieldJsonAdapter())
        val gson: Gson = builder.create()

        var testParse = """
            {
              "title": "Insurance",
              "footer": "I wish to purchase excess reimbursement insurance of 13.95per day = 13.95 (Approx 13.95) Please Note: Excess insurance is billed separately on your credit card statement.",
              "fields": [
                {
                  "type": "switch",
                  "id": "insuranceOption",
                  "title": "Annual Excess Insurance",
                  "value": false
                }
              ]
            }
        """.trimIndent()

        var reader = JsonReader(StringReader(testParse))
        reader.isLenient = true

        val actual: FormGroup = gson.fromJson(reader, FormGroup::class.java)
        assertEquals("Insurance", actual.title)
        assertEquals(
            "I wish to purchase excess reimbursement insurance of 13.95per day = 13.95 (Approx 13.95) Please Note: Excess insurance is billed separately on your credit card statement.",
            actual.footer
        )
        assertEquals(1, actual.fields.size)
        assert(actual.fields[0] is SwitchFormField)

        testParse = """
            {
              "title": "Ticket",
              "fields": [
                {
                  "type": "stepper",
                  "title": "Adult",
                  "id": "adult",
                  "required": true,
                  "value": 1,
                  "minValue": 0,
                  "maxValue": 15
                },
                {
                  "type": "stepper",
                  "title": "Child",
                  "id": "child",
                  "required": false,
                  "value": 0,
                  "minValue": 0,
                  "maxValue": 15
                },
                {
                  "type": "option",
                  "title": "Type",
                  "id": "ticket_type",
                  "value": {
                    "title": "One way",
                    "value": "one_way"
                  },
                  "allValues": [
                    {
                      "title": "One way",
                      "value": "one_way"
                    },
                    {
                      "title": "Return",
                      "value": "return"
                    }
                  ]
                }
              ]
            }
        """.trimIndent()

        reader = JsonReader(StringReader(testParse))
        reader.isLenient = true

        val actualSecond: FormGroup = gson.fromJson(reader, FormGroup::class.java)
        assertEquals("Ticket", actualSecond.title)
        assertEquals(3, actualSecond.fields.size)
        assert(actualSecond.fields[0] is StepperFormField)
        assert(actualSecond.fields[1] is StepperFormField)
        assert(actualSecond.fields[2] is OptionFormField)
    }

    @Test
    fun parcelableTest() {
        // Step 1: Create the expected FormGroup with different FormField types
        val expect = FormGroup()
        val list = mutableListOf<FormField>()

        // Add StringFormField instances
        for (i in 0 until 3) {
            val item = StringFormField().apply { setValue(i.toString()) }
            list.add(item)
        }

        // Add LinkFormField instance
        val linkFormField = LinkFormField().apply { setValue("a") }
        list.add(linkFormField)

        // Add SwitchFormField instance
        val switchFormField = SwitchFormField().apply { setValue(false) }
        list.add(switchFormField)

        expect.fields = list

        // Step 2: Mock the Parcel object
        val parcel = mockk<Parcel>(relaxed = true)

        // Step 3: Write to the parcel
        every { parcel.writeString(any()) } returns Unit
        every { parcel.writeTypedList(any<List<FormField>>()) } returns Unit

        // Trigger writeToParcel on the object
        expect.writeToParcel(parcel, 0)

        // Verify that the fields are written correctly to the parcel
        verify { parcel.writeString(expect.title) }
        verify { parcel.writeString(expect.footer) }
        verify { parcel.writeTypedList(expect.fields) }

        // Step 4: Simulate reading from the parcel
        every { parcel.readString() } returnsMany listOf(expect.title, expect.footer)
        every { parcel.createTypedArrayList(FormField.CREATOR) } returns ArrayList(list)

        // Recreate FormGroup from the parcel
        val actual = FormGroup.CREATOR.createFromParcel(parcel)

        // Step 5: Assertions
        assertEquals(expect.fields.size, actual.fields.size)
        assert(actual.fields[0] is StringFormField)
        assertEquals("0", (actual.fields[0] as StringFormField).getValue())
        assert(actual.fields[1] is StringFormField)
        assertEquals("1", (actual.fields[1] as StringFormField).getValue())
        assert(actual.fields[2] is StringFormField)
        assertEquals("2", (actual.fields[2] as StringFormField).getValue())
        assert(actual.fields[3] is LinkFormField)
        assertEquals("a", (actual.fields[3] as LinkFormField).getValue())
        assert(actual.fields[4] is SwitchFormField)
        assertEquals(false, (actual.fields[4] as SwitchFormField).getValue())
    }
}