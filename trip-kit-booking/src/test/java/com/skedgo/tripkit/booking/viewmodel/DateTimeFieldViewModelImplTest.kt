package com.skedgo.tripkit.booking.viewmodel

import android.os.Parcel
import com.skedgo.tripkit.booking.DateTimeFormField
import com.skedgo.tripkit.booking.viewmodel.DateTimeFieldViewModelImpl.CREATOR.createFromParcel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import org.assertj.core.api.Java6Assertions
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
class DateTimeFieldViewModelImplTest {

    /**
     * FIXME: Temporarily ignore as it's flaky by the use of {@link Calendar#getInstance()}.
     */
    @Ignore
    @Test
    fun shouldReflectSomeValuesFromField() {
        val field = DateTimeFormField().apply {
            setValue(1234567890)
            title = "Time"
        }
        val viewModel = DateTimeFieldViewModelImpl.create(field)

        assertEquals("Time", viewModel.getTitle(), "Should reflect title from field")
        assertEquals("Sat, 14 Feb 2009", viewModel.getDate(), "Should format date from field")
        assertEquals("06:31", viewModel.getTime(), "Should format time from field")
        assertEquals(14, viewModel.getDay())
        assertEquals(1, viewModel.getMonth())
        assertEquals(2009, viewModel.getYear())
        assertEquals(6, viewModel.getHour())
        assertEquals(31, viewModel.getMinute())
    }

    /**
     * FIXME: Temporarily ignore as it's flaky by the use of {@link Calendar#getInstance()}.
     */
    @Ignore
    @Test
    fun shouldDisplayHourIn24hFormat() {
        val field = DateTimeFormField().apply {
            setValue(1445235929)
            title = "Time"
        }
        val viewModel = DateTimeFieldViewModelImpl.create(field)
        assertEquals(13, viewModel.getHour())
    }

    @Test
    fun shouldBeParcelledProperly() {
        val field = DateTimeFormField().apply {
            setValue(1234567890)
            title = "Time"
        }
        val expected = DateTimeFieldViewModelImpl.create(field)

        // Mock the Parcel
        val parcel = mockk<Parcel>(relaxed = true)

        // Mock the behavior for writing Parcelable
        every { parcel.writeParcelable(any(), any()) } just Runs
        every { parcel.writeSerializable(any()) } just Runs

        // Perform writeToParcel with mocked Parcel
        expected.writeToParcel(parcel, 0)

        // Mock reading from Parcel - this step is crucial when you test the CREATOR
        every { parcel.readParcelable<DateTimeFormField>(any<ClassLoader>()) } returns field
        every { parcel.readSerializable() } returns Calendar.getInstance()

        // Set the data position to zero (simulates resetting the Parcel for reading)
        every { parcel.setDataPosition(0) } just Runs

        // Use the CREATOR to create a new instance from the Parcel
        val actual = DateTimeFieldViewModelImpl.CREATOR.createFromParcel(parcel)

        // Perform assertions
        assertEquals("Title should be parcelled properly", expected.getTitle(), actual.getTitle())
        assertEquals("Date should parcelled properly", expected.getDate(), actual.getDate())
        assertEquals("Time should parcelled properly", expected.getTime(), actual.getTime())
    }

    @Test
    fun shouldShowSelectedValueProperly() {
        val field = DateTimeFormField().apply {
            setValue(1234567890)
            title = "Time"
        }
        val viewModel = DateTimeFieldViewModelImpl.create(field)
        viewModel.setTime(23, 59)
        viewModel.setDate(2015, 10, 20)

        assertEquals("Should reflect new date selected","Fri, 20 Nov 2015", viewModel.getDate())
        assertEquals("Should reflect new time selected", "23:59", viewModel.getTime())
    }
}