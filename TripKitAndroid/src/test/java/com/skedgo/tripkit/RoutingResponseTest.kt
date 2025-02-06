package com.skedgo.tripkit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonPrimitive
import com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert
import com.skedgo.tripkit.common.model.realtimealert.RealtimeAlerts.getDisplayText
import com.skedgo.tripkit.common.util.Gsons.createForLowercaseEnum
import com.skedgo.tripkit.routing.RoutingResponse
import com.skedgo.tripkit.routing.Trip
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import thuytrinh.mockwebserverrule.MockWebServerRule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoutingResponseTest {
    // TODO: Unit test - refactor
    @Test
    fun processDirectionTemplate() {
        // Case 1
        var serviceDirectionNode = JsonPrimitive("Service direction")
        var notes = "This is a <DIRECTION>"
        var result = RoutingResponse.processDirectionTemplate(serviceDirectionNode, notes, null)
        assertEquals("This is a Direction: Service direction", result)

        // Case 2
        serviceDirectionNode = JsonPrimitive("")
        notes = "This is a <DIRECTION>"
        result = RoutingResponse.processDirectionTemplate(serviceDirectionNode, notes, null)
        assertEquals("This is a ", result)

        // Case 3
        notes = "This is a <DIRECTION>"
        result = RoutingResponse.processDirectionTemplate(null, notes, null)
        assertEquals("This is a ", result)

        // Case 4
        result = RoutingResponse.processDirectionTemplate(null, "", null)
        assertEquals("", result)
    }

    @Test
    @Throws(IOException::class)
    fun tripHasReferenceToGroup() {
        val mockResponse = MockWebServerRule.createMockResponse("/routing0.json")
        val routingJson = mockResponse.getBody()?.readUtf8()

        val gson = createForLowercaseEnum()
        val response = gson.fromJson(routingJson, RoutingResponse::class.java)
        response.processRawData(
            ApplicationProvider.getApplicationContext<Context>().resources,
            gson
        )

        for (group in response.tripGroupList!!) {
            assertThat(group.trips)
                .describedAs("Trip must have reference to its group")
                .are(object : Condition<Trip>() {
                    override fun matches(value: Trip): Boolean {
                        return value.group == group
                    }
                })
        }
    }

    @Test
    @Throws(IOException::class)
    fun shouldParseProperly() {
        val mockResponse = MockWebServerRule.createMockResponse("/routing0.json")
        val routingJson = mockResponse.getBody()?.readUtf8()

        val gson = createForLowercaseEnum()
        val response = gson.fromJson(routingJson, RoutingResponse::class.java)
        response.processRawData(
            ApplicationProvider.getApplicationContext<Context>().resources,
            gson
        )

        assertThat(response).isNotNull()
        assertThat(response.alerts).hasSize(1)
        assertThat(response.tripGroupList).hasSize(1).doesNotContainNull()

        val group = response.tripGroupList!![0]
        assertThat(group.trips).hasSize(1).doesNotContainNull()

        val trip = group.trips!![0]
        assertThat(trip.segmentList).hasSize(1).doesNotContainNull()

        val motorbikeSegment = trip.segmentList[0]
        assertThat(motorbikeSegment.alerts).hasSize(1).doesNotContainNull()

        val alert = motorbikeSegment.alerts!![0]
        assertThat(alert.severity()).isEqualTo(RealtimeAlert.SEVERITY_WARNING)
        assertThat(alert.title()).isEqualTo("Traffic delay")
        assertThat(getDisplayText(alert))
            .isEqualTo("Unusually high traffic on the route.")
    }

    @Test
    @Throws(IOException::class)
    fun parseMultipleStreets() {
        val mockResponse = MockWebServerRule.createMockResponse("/routingStreets.json")
        val routingJson = mockResponse.getBody()?.readUtf8()

        val gson = createForLowercaseEnum()
        val response = gson.fromJson(routingJson, RoutingResponse::class.java)
        response.processRawData(
            ApplicationProvider.getApplicationContext<Context>().resources,
            gson
        )

        assertThat(response).isNotNull()
        assertThat(response.tripGroupList).hasSize(1).doesNotContainNull()

        val group = response.tripGroupList!![0]
        assertThat(group.trips).hasSize(1).doesNotContainNull()

        val trip = group.trips!![0]
        assertThat(trip.segmentList).hasSize(1).doesNotContainNull()

        val motorbikeSegment = trip.segmentList[0]
        assertThat(motorbikeSegment.streets).hasSize(4).doesNotContainNull()
    }
}