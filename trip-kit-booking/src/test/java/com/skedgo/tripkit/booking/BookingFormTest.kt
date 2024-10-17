package com.skedgo.tripkit.booking

import android.os.Parcel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.skedgo.tripkit.booking.AddressFormField
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingForm.CREATOR.createFromParcel
import com.skedgo.tripkit.extensions.fromJson
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import java.io.StringReader
import java.nio.charset.Charset

@RunWith(RobolectricTestRunner::class)
class BookingFormTest {

    private lateinit var gson: Gson

    @Before
    fun before() {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(FormField::class.java, FormFieldJsonAdapter())
        gson = builder.create()
    }

    @Test
    fun parse() {
        // Test 1
        var testParse = """
            {
              "form": [
                {
                  "fields": [
                    {
                      "type": "DATETIME",
                      "title": "Time",
                      "id": "time",
                      "value": 1410871325000
                    }
                  ]
                }
              ],
              "action": {
                "title": "Done"
              },
              "title": "Car2Go",
              "subtitle": "Tue 16 22:42",
              "message": "A confirmation email has been sent"
            }
        """.trimIndent()

        var reader = JsonReader(StringReader(testParse))
        reader.isLenient = true
        var actual = gson.fromJson<BookingForm>(reader, BookingForm::class.java)

        assertEquals("Car2Go", actual.title)
        assertEquals("Tue 16 22:42", actual.subtitle)
        assertEquals(1, actual.form.size)

        val item = actual.form[0]
        assertEquals(1, item.fields.size)
        assert(item.fields[0] is DateTimeFormField)

        // Test 2
        testParse = """
            {
              "type": "bookingForm",
              "title": "Login to Car2Go",
              "action": {
                "title": "Next",
                "enabled": false,
                "url": "http://BARYOGENESIS.SKEDGO.COM/satapp/booking/cba0b388-4b8e-4812-b287-c7302327c794/book"
              },
              "form": [
                {
                  "title": "Authorize",
                  "fields": [
                    {
                      "type": "link",
                      "title": "Go to Car2Go",
                      "id": "oauth",
                      "value": "https://www.car2go.com/api/authorize?oauth_token=QAd66gisPl80PIDy1sywFUHB"
                    }
                  ]
                }
              ]
            }
        """.trimIndent()
        reader = JsonReader(StringReader(testParse))
        reader.isLenient = true
        actual = gson.fromJson(reader, BookingForm::class.java)
        assert(actual.form != null)

        // Test 3
        testParse = """
            {
              "type": "bookingForm",
              "title": "Car2Go",
              "form": [
                {
                  "title": "Vehicle",
                  "fields": [
                    {
                      "type": "bookingForm",
                      "title": "SmartCar 690XBP",
                      "action": {
                        "title": "Next",
                        "url": "http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book"
                      },
                      "form": [
                        {
                          "title": "Trip",
                          "fields": [
                            {
                              "type": "string",
                              "title": "Car",
                              "id": "car",
                              "hidden": true,
                              "value": "690XBP",
                              "keyboardType": "TEXT"
                            },
                            {
                              "type": "address",
                              "title": "Location",
                              "id": "location",
                              "readOnly": true,
                              "value": {
                                "lat": 39.75741,
                                "lng": -105.03937,
                                "address": "Perry St 2805, 80212 Denver",
                                "name": "Perry St 2805, 80212 Denver"
                              }
                            }
                          ]
                        }
                      ],
                      "subtitle": "Selected Car"
                    }
                  ]
                }
              ],
              "subtitle": "From $10.57"
            }
        """.trimIndent()
        reader = JsonReader(StringReader(testParse))
        reader.isLenient = true
        actual = gson.fromJson(reader, BookingForm::class.java)
        assertEquals(null, actual.refreshURLForSourceObject)
        assertEquals("Car2Go", actual.title)
        assertEquals("From $10.57", actual.subtitle)
        assertEquals(1, actual.form.size)

        val items = actual.form[0].fields
        assert(items[0] is BookingForm)
    }

    @Test
    fun parcelable() {
        val testParse = """
        {
          "type": "bookingForm",
          "title": "Jay Ride Shuttle",
          "action": {
            "title": "Next",
            "url": "http://bb-server.buzzhives.com/satapp-debug/booking/0000000a-000a-000a-000a-00000000000a/book"
          },
          "form": [
            {
              "title": "Trip",
              "fields": [
                {
                  "type": "address",
                  "title": "From",
                  "id": "from",
                  "value": {
                    "lat": -33.8004829805011,
                    "lng": 151.283876064691,
                    "address": "Manly Wharf",
                    "name": "Manly Wharf"
                  }
                }
              ]
            }
          ]
        }
    """.trimIndent()

        val reader = JsonReader(StringReader(testParse))
        reader.isLenient = true
        val expected: BookingForm = gson.fromJson(reader, BookingForm::class.java)

        // Mock the Parcel
        val parcel = mockk<Parcel>(relaxed = true)

        // Mock writing to Parcel
        every { parcel.writeInt(any()) } just Runs
        every { parcel.writeParcelable(any(), any()) } just Runs
        every { parcel.writeTypedList(any<List<FormGroup>>()) } just Runs
        every { parcel.writeString(any()) } just Runs

        // Write to parcel
        expected.writeToParcel(parcel, 0)

        // Mock resetting the data position
        every { parcel.setDataPosition(0) } just Runs

        // Mock reading from Parcel
        every { parcel.readInt() } returns 7 // Assuming BOOKINGFORM constant is 7
        every { parcel.readString() } returnsMany listOf(
            expected.title,
            expected.refreshURLForSourceObject,
            expected.imageUrl
        )
        every { parcel.readParcelable<BookingAction>(any<ClassLoader>()) } returns expected.action
        every { parcel.createTypedArrayList(FormGroup.CREATOR) } returns ArrayList(expected.form)

        // Create actual from parcel
        val actual: BookingForm = BookingForm.CREATOR.createFromParcel(parcel)

        // Perform assertions
        val actualAction: BookingAction? = actual.action
        assertEquals(expected.action?.url, actualAction?.url)
        assertEquals(expected.refreshURLForSourceObject, actual.refreshURLForSourceObject)
    }
}