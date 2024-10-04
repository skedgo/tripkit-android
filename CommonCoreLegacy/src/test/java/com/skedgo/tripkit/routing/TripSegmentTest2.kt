package com.skedgo.tripkit.routing

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.R
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit.MINUTES

@RunWith(AndroidJUnit4::class)
class TripSegmentTest2 {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var resources: Resources

    @Before
    fun setUp() {
        context = mockk()
        resources = mockk()

        every { context.resources } returns resources
    }

    @Test
    fun shouldShowHideSegmentProperly() {
        val segment = TripSegment()
        segment.visibility = Visibilities.VISIBILITY_IN_DETAILS
        assertThat(segment.isVisibleInContext(null)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_DETAILS)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_SUMMARY)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_ON_MAP)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_HIDDEN)).isFalse()

        segment.visibility = Visibilities.VISIBILITY_IN_SUMMARY
        assertThat(segment.isVisibleInContext(null)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_DETAILS)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_SUMMARY)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_ON_MAP)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_HIDDEN)).isFalse()

        segment.visibility = Visibilities.VISIBILITY_ON_MAP
        assertThat(segment.isVisibleInContext(null)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_DETAILS)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_SUMMARY)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_ON_MAP)).isTrue()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_HIDDEN)).isFalse()

        segment.visibility = Visibilities.VISIBILITY_HIDDEN
        assertThat(segment.isVisibleInContext(null)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_DETAILS)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_IN_SUMMARY)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_ON_MAP)).isFalse()
        assertThat(segment.isVisibleInContext(Visibilities.VISIBILITY_HIDDEN)).isFalse()
    }

    @Test
    fun shouldGiveCorrectRealtimeStatusText() {
        every { resources.getString(R.string.live_traffic) } returns "Live traffic"
        every { resources.getString(R.string.real_minustime) } returns "Real-time minus"

        val car = ModeInfo().apply { localIconName = "car" }
        val segment = TripSegment().apply {
            isRealTime = true
            modeInfo = car
        }
        assertThat(segment.getRealTimeStatusText(resources)).isEqualTo("Live traffic")

        segment.isRealTime = false
        assertThat(segment.getRealTimeStatusText(resources)).isNullOrEmpty()

        val train = ModeInfo().apply { localIconName = "train" }
        segment.modeInfo = train
        segment.isRealTime = true
        assertThat(segment.getRealTimeStatusText(resources)).isEqualTo("Real-time minus")

        segment.isRealTime = false
        assertThat(segment.getRealTimeStatusText(resources)).isNullOrEmpty()
    }

    @Test
    fun darkVehicleIcon() {
        val ferry = ModeInfo().apply { localIconName = "ferry" }
        val segment = TripSegment().apply {
            modeInfo = ferry
            isRealTime = true
        }

        every { resources.getDrawable(R.drawable.ic_ferry_realtime, null) } returns mockk() // Mock the drawable
        assertThat(segment.darkVehicleIcon).describedAs("Should give correct icon for realtime ferry")
            .isEqualTo(R.drawable.ic_ferry_realtime)

        segment.isRealTime = false
        every { resources.getDrawable(R.drawable.ic_ferry, null) } returns mockk() // Mock the drawable
        assertThat(segment.darkVehicleIcon).describedAs("Should give correct icon for non-realtime ferry")
            .isEqualTo(R.drawable.ic_ferry)
    }

    @Test
    fun shouldHandleNullityForDisplayNotes() {
        val segment = TripSegment().apply {
            notes = null
            startTimeInSecs = MINUTES.toSeconds(2)
            endTimeInSecs = MINUTES.toSeconds(3)
        }

        assertThat(segment.getDisplayNotes(context, true)).isNull()
    }

    @Test
    fun displayNotesShouldHavePlatformTemplateProcessed() {

        every { resources.getQuantityString(any(), any()) } returns "Platform: Platform 2"

        val segment = TripSegment().apply {
            notes = "To City Circle. <PLATFORM>"
            startTimeInSecs = MINUTES.toSeconds(2)
            endTimeInSecs = MINUTES.toSeconds(3)
            platform = "Platform 2"
        }

        assertThat(segment.getDisplayNotes(context, true)).isEqualTo("To City Circle. Platform: Platform 2")
    }

    @Test
    fun displayNotesShouldHaveStopsTemplateProcessed() {

        every { resources.getQuantityString(R.plurals.number_of_stops, any()) } returns "3 stops"

        val segment = TripSegment().apply {
            notes = "To City Circle. <STOPS>"
            startTimeInSecs = MINUTES.toSeconds(2)
            endTimeInSecs = MINUTES.toSeconds(3)
            stopCount = 3
        }

        assertThat(segment.getDisplayNotes(context, true)).isEqualTo("To City Circle. 3 stops")
    }
}