package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.ui.BuildConfig
import com.skedgo.android.tripkit.booking.ui.TestRunner
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject

@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class FieldBookingFormViewModelTest {

  @Test fun shouldSetTitle() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.title).thenReturn("title")

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.title).isEqualTo("title")

  }

  @Test fun shouldSetEmptyTitle() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.title).thenReturn(null)

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.title).isEqualTo("")

  }

  @Test fun shouldSetSubTitle() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.subtitle).thenReturn("subtitle")

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.subTitle).isEqualTo("subtitle")

  }

  @Test fun shouldSetEmptySubTitle() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.subtitle).thenReturn(null)

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.subTitle).isEqualTo("")

  }

  @Test fun shouldHaveImageUrl() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.imageUrl).thenReturn("url")

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.hasImageUrl).isTrue()

  }

  @Test fun shouldNotHaveImageUrl() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.imageUrl).thenReturn(null)

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.hasImageUrl).isFalse()

  }

  @Test fun shouldSetImageUrl() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.imageUrl).thenReturn("url")

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.imageUrl).isEqualTo("url")

  }

  @Test fun shouldSetNullImageUrl() {

    val bookingFormFormField: BookingForm = mock()
    whenever(bookingFormFormField.imageUrl).thenReturn(null)

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingFormFormField, PublishSubject.create())

    assertThat(viewModel.imageUrl).isEqualTo(null)

  }

  @Test fun shouldEmitBookingForm() {

    val bookingForm: BookingForm = mock()

    val onBookingForm: PublishSubject<BookingForm> = PublishSubject.create()

    val subscriber = TestSubscriber<BookingForm>()
    onBookingForm.subscribe(subscriber)

    val viewModel: FieldBookingFormViewModel = FieldBookingFormViewModel(bookingForm, onBookingForm)

    viewModel.onBookingFormAction()

    subscriber.assertValue(bookingForm)

  }

}