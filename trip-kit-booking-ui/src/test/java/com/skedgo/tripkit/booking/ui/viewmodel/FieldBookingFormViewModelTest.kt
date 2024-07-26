package com.skedgo.tripkit.booking.ui.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.BookingForm
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FieldBookingFormViewModelTest {

    @Test
    fun shouldSetTitle() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.title).thenReturn("title")

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.title).isEqualTo("title")

    }

    @Test
    fun shouldSetEmptyTitle() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.title).thenReturn(null)

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.title).isEqualTo("")

    }

    @Test
    fun shouldSetSubTitle() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.subtitle).thenReturn("subtitle")

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.subTitle).isEqualTo("subtitle")

    }

    @Test
    fun shouldSetEmptySubTitle() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.subtitle).thenReturn(null)

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.subTitle).isEqualTo("")

    }

    @Test
    fun shouldHaveImageUrl() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.imageUrl).thenReturn("url")

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.hasImageUrl).isTrue()

    }

    @Test
    fun shouldNotHaveImageUrl() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.imageUrl).thenReturn(null)

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.hasImageUrl).isFalse()

    }

    @Test
    fun shouldSetImageUrl() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.imageUrl).thenReturn("url")

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.imageUrl).isEqualTo("url")

    }

    @Test
    fun shouldSetNullImageUrl() {

        val bookingFormFormField: BookingForm = mock()
        whenever(bookingFormFormField.imageUrl).thenReturn(null)

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

        assertThat(viewModel.imageUrl).isEqualTo(null)

    }

    @Test
    fun shouldEmitBookingForm() {

        val bookingForm: BookingForm = mock()

        val onBookingForm: PublishSubject<BookingForm> = PublishSubject.create()

        val subscriber = onBookingForm.test()

        val viewModel: FieldBookingFormViewModel =
            FieldBookingFormViewModel(bookingForm, onBookingForm)

        viewModel.onBookingFormAction()

        subscriber.assertValue(bookingForm)

    }

}