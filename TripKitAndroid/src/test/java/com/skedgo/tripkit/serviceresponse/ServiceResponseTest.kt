package com.skedgo.tripkit.serviceresponse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.skedgo.tripkit.ServiceResponse
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.mock.MockServiceResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ServiceResponseTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    @Throws(IOException::class)
    fun testSerialize() {
        val response = Gson().fromJson(
            MockServiceResponse.SERVICE_RESPONSE_TEST,
            ServiceResponse::class.java
        )

        assertThat(response).isNotNull
        assertThat(response.realTimeStatus()).isEqualTo("CAPABLE")
        assertThat(response.shapes())
            .isNotNull
            .hasSize(1)
            .doesNotContainNull()

        val firstShape = response.shapes()[0]
        assertThat(firstShape.stops)
            .isNotNull
            .hasSize(26)
    }

    @Test
    @Throws(IOException::class)
    fun testUpdatedExample() {
        val response = Gson().fromJson(
            MockServiceResponse.SERVICE_RESPONSE_TEST_STOPS,
            ServiceResponse::class.java
        )

        assertThat(response).isNotNull
        assertThat(response.realTimeStatus()).isEqualTo("CAPABLE")
        assertThat(response.shapes())
            .isNotNull
            .hasSize(1)
            .doesNotContainNull()

        val firstShape = response.shapes()[0]
        assertThat(firstShape.stops)
            .isNotNull
            .hasSize(58)
    }

    @Test
    @Throws(IOException::class)
    fun testRealTimeVehicle() {
        val response = Gson().fromJson(
            MockServiceResponse.SERVICE_RESPONSE_TEST_REAL_TIME,
            ServiceResponse::class.java
        )

        assertThat(response).isNotNull
        assertThat(response.realTimeStatus()).isEqualTo("IS_REAL_TIME")
        assertThat(response.shapes())
            .isNotNull
            .hasSize(1)
            .doesNotContainNull()

        val firstShape = response.shapes()[0]
        assertThat(firstShape.stops)
            .isNotNull
            .hasSize(41)

        val vehicle = response.realtimeVehicle()

        assertThat(vehicle).isNotNull
        assertThat(vehicle?.lastUpdateTime).isEqualTo(1468594846)
        assertThat(vehicle?.location).isNotNull

        val location = vehicle?.location

        assertThat(location?.lat).isEqualTo(-33.86224)
        assertThat(location?.lon).isEqualTo(151.21207)
        assertThat(location?.bearing).isEqualTo(6)

        assertThat(response.realtimeAlternativeVehicle())
            .isNotNull
            .hasSize(1)
            .doesNotContainNull()
    }
}
