package com.skedgo.tripkit.booking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.booking.OptionFormField
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OptionValueTest {

    @Test
    fun parseTest() {
        val gson: Gson = GsonBuilder().serializeNulls().create()

        val testParse = """
            {
              "type": "OPTION",
              "title": "Account",
              "id": "account",
              "value": {
                "title": "100 - Test Account 1",
                "value": "100 - Test Account 1"
              },
              "allValues": [
                {
                  "title": "100 - Test Account 1",
                  "value": "100 - Test Account 1"
                },
                {
                  "title": "200 - Test Account 1",
                  "value": "200 - Test Account 2"
                }
              ]
            }
        """.trimIndent()

        val actual: OptionFormField = gson.fromJson(testParse, OptionFormField::class.java)

        // Assertions
        assertEquals("OPTION", actual.type)
        assertEquals("Account", actual.title)
        assertEquals("account", actual.id)
        assertEquals("100 - Test Account 1", actual.getValue()?.title)
        assertEquals("100 - Test Account 1", actual.getValue()?.value)

        val allValues = actual.allValues
        assertEquals(2, allValues.size)
        assertEquals("100 - Test Account 1", allValues[0].title)
        assertEquals("100 - Test Account 1", allValues[0].value)
        assertEquals("200 - Test Account 1", allValues[1].title)
        assertEquals("200 - Test Account 2", allValues[1].value)
    }
}