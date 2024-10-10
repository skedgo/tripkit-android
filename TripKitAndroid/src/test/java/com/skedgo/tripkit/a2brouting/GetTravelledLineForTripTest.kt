package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.common.model.Street
import com.skedgo.tripkit.common.model.TransportMode
import org.amshove.kluent.mock
import org.amshove.kluent.`should equal`
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetTravelledLineForTripTest {

    val getTravelledLineForTrip = GetTravelledLineForTrip()

    @Test
    fun `should return green for safe street with bicycle or wheelchair`() {
        val street = mock<Street>().apply {
            whenever(this.safe()).thenReturn(true)
        }
        getTravelledLineForTrip.getColorForWheelchairAndBicycle(
            street = street,
            modeId = TransportMode.ID_BICYCLE
        ) `should equal` Color.GREEN
        getTravelledLineForTrip.getColorForWheelchairAndBicycle(
            street = street,
            modeId = TransportMode.ID_WHEEL_CHAIR
        ) `should equal` Color.GREEN
    }

    @Test
    fun `should return null modes other than bicycle and wheelchair`() {
        getTravelledLineForTrip.getColorForWheelchairAndBicycle(
            mock(),
            modeId = TransportMode.ID_CAR
        ) `should equal` null
    }

    @Test
    fun `should return red for dismount street with bicycle`() {
        val street = mock<Street>().apply {
            whenever(this.dismount()).thenReturn(true)
        }
        getTravelledLineForTrip.getColorForWheelchairAndBicycle(
            street = street,
            modeId = TransportMode.ID_BICYCLE
        ) `should equal` Color.RED
    }

    @Test
    fun `should return yellow for unsafe street with bicycle or wheelchair`() {
        val street = mock<Street>().apply {
            whenever(this.safe()).thenReturn(false)
            whenever(this.dismount()).thenReturn(false)
        }
        getTravelledLineForTrip.getColorForWheelchairAndBicycle(
            street = street,
            modeId = TransportMode.ID_WHEEL_CHAIR
        ) `should equal` Color.YELLOW
        getTravelledLineForTrip.getColorForWheelchairAndBicycle(
            street = street,
            modeId = TransportMode.ID_BICYCLE
        ) `should equal` Color.YELLOW
    }
}