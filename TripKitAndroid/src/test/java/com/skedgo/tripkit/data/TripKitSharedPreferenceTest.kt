package com.skedgo.tripkit.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_CLIENT_ID
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_POLYGON
import com.skedgo.tripkit.account.data.Polygon
import com.skedgo.tripkit.extensions.fromJson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TripKitSharedPreferenceTest {

    companion object {
        const val TEST_CLIENT_ID = "test_client_id"
        const val MOCK_POLYGON_DATE =
            """
                {
                  "polygon": {
                    "type": "MultiPolygon",
                    "coordinates": [
                      [
                        [
                          [
                            -82.15809360496644,
                            43.95196447506092
                          ],
                          [
                            -81.86016289579419,
                            41.72524481319121
                          ],
                          [
                            -85.9210396251596,
                            41.06445418951597
                          ],
                          [
                            -82.15809360496644,
                            43.95196447506092
                          ]
                        ]
                      ]
                    ]
                  }
                }
            """
    }

    private lateinit var tripKitSharedPreference: TripKitSharedPreference
    private val context = mockk<Context>()
    private val sharedPreferences = mockk<SharedPreferences>()
    private val editor = mockk<SharedPreferences.Editor>(relaxed = true)
    private val gson = Gson()

    @Before
    fun setUp() {
        every { context.getSharedPreferences(any(), any()) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        tripKitSharedPreference = TripKitSharedPreference(context)
    }

    @Test
    fun `saveClientId - verify SharedPreference editor is called to save clientId `() {
        tripKitSharedPreference.saveClientId(TEST_CLIENT_ID)

        verify { editor.putString(PREF_KEY_CLIENT_ID, TEST_CLIENT_ID).apply() }
    }

    @Test
    fun `getClientId - should return expected client id`() {
        every { sharedPreferences.getString(PREF_KEY_CLIENT_ID, null) } returns TEST_CLIENT_ID

        val result = tripKitSharedPreference.getClientId()

        assertEquals(TEST_CLIENT_ID, result)
    }

    @Test
    fun `savePolygon - verify SharedPreference editor is called to save polygon data in string`() {
        val polygon: Polygon = gson.fromJson(MOCK_POLYGON_DATE)
        val polygonJson = gson.toJson(polygon)
        tripKitSharedPreference.savePolygon(polygon)

        verify { editor.putString(PREF_KEY_POLYGON, polygonJson).apply() }
    }

    @Test
    fun `getPolygon - should return expected polygon data`() {
        val polygon: Polygon = gson.fromJson(MOCK_POLYGON_DATE)
        val polygonJson = gson.toJson(polygon)
        every { sharedPreferences.getString(PREF_KEY_POLYGON, "") } returns polygonJson

        val result = tripKitSharedPreference.getPolygon()

        assertEquals(polygon, result)
    }
}