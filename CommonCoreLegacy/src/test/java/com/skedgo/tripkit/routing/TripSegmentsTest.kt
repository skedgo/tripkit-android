package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripSegmentsTest {
    @Test
    fun shouldReturnColorForPublicTransport() {
        val color = ServiceColor(1, 2, 3)
        val segment = TripSegment()
        segment.serviceColor = color
        Java6Assertions.assertThat(TripSegments.getTransportColor(segment)).isSameAs(color)
    }

    @Test
    fun shouldReturnColorForPrivateTransport() {
        val color = ServiceColor(1, 2, 3)
        val modeInfo = ModeInfo()
        modeInfo.color = color
        val segment = TripSegment()
        segment.modeInfo = modeInfo
        Java6Assertions.assertThat(TripSegments.getTransportColor(segment)).isSameAs(color)
    }

    @Test
    fun shouldReturnNullTransportColor() {
        Java6Assertions.assertThat(TripSegments.getTransportColor(null)).isNull()
        Java6Assertions.assertThat(TripSegments.getTransportColor(TripSegment())).isNull()
    }

    @Test
    fun shouldPickColorForPublicTransportFirst() {
        val privateTransportColor = ServiceColor(1, 2, 3)
        val modeInfo = ModeInfo()
        modeInfo.color = privateTransportColor
        val segment = TripSegment()
        segment.modeInfo = modeInfo
        val publicTransportColor = ServiceColor(4, 5, 6)
        segment.serviceColor = publicTransportColor
        Java6Assertions.assertThat(TripSegments.getTransportColor(segment))
            .isSameAs(publicTransportColor)
    }
}